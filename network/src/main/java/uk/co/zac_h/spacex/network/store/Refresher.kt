package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import uk.co.zac_h.spacex.core.database.FetchMetadataDao
import uk.co.zac_h.spacex.core.database.FetchMetadataEntity
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.TooManyRequestsException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

/**
 * Decides when stored data needs fetching again, and records when it was fetched. Launch Library
 * allows only 15 calls per hour per IP, so data is refetched only once it is older than its window.
 */
@Singleton
class Refresher @Inject constructor(
    private val metadataDao: FetchMetadataDao,
    private val clock: Clock
) {

    // One refresh per key at a time, so two screens opening together spend one call.
    private val mutexes = ConcurrentHashMap<String, Mutex>()

    /** When the data under [key] was last fetched, in milliseconds since the epoch, or null if never. */
    fun fetchedAt(key: String): Flow<Long?> = metadataDao.observe(key).map { it?.fetchedAt }

    /** Records that the data under [key] is current as of now, e.g. when a wider fetch covered it. */
    suspend fun markFetched(key: String) {
        metadataDao.upsert(FetchMetadataEntity(key = key, fetchedAt = clock.now()))
    }

    suspend fun isStale(key: String, maxAge: Duration): Boolean {
        val fetchedAt = metadataDao.get(key)?.fetchedAt ?: return true
        return clock.now() - fetchedAt >= maxAge.inWholeMilliseconds
    }

    /**
     * Runs [fetch] unless the data under [key] is younger than [maxAge] and [force] is false, and
     * records the fetch time when it succeeds. Returns [ApiResult.Success] when the stored data is
     * current, or [ApiResult.Failure] when the fetch failed; stored data is untouched on failure.
     */
    suspend fun refresh(
        key: String,
        maxAge: Duration,
        force: Boolean = false,
        fetch: suspend () -> Unit
    ): ApiResult<Unit> = mutexes.getOrPut(key) { Mutex() }.withLock {
        if (!force && !isStale(key, maxAge)) return@withLock ApiResult.Success(Unit)
        try {
            fetch()
            markFetched(key)
            ApiResult.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            ApiResult.Failure(
                when (e.code()) {
                    429 -> TooManyRequestsException(
                        runCatching { e.response()?.errorBody()?.string() }.getOrNull()
                            ?.filter { it.isDigit() }?.toIntOrNull()
                    )
                    else -> Exception(e.message())
                }
            )
        } catch (e: Exception) {
            ApiResult.Failure(e)
        }
    }
}
