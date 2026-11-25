package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import uk.co.zac_h.spacex.core.database.CachedItemDao
import uk.co.zac_h.spacex.core.database.CachedItemEntity
import uk.co.zac_h.spacex.core.database.FetchMetadataDao
import uk.co.zac_h.spacex.core.database.FetchMetadataEntity
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.TooManyRequestsException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

/**
 * A list of API models stored in the database under [key]: the screens observe [items], and
 * [refresh] fills it from the network only when it is older than its window.
 */
class CachedList<T>(
    private val key: String,
    private val serializer: KSerializer<T>,
    private val id: (T) -> String,
    private val itemDao: CachedItemDao,
    private val metadataDao: FetchMetadataDao,
    private val json: Json,
    private val clock: Clock
) {

    // One fetch per list at a time, so two screens opening together spend one call.
    private val mutex = Mutex()

    /** The stored items, in the order the API returned them. Items that no longer decode are skipped. */
    val items: Flow<List<T>> = itemDao.observe(key).map { entities ->
        entities.mapNotNull { runCatching { json.decodeFromString(serializer, it.json) }.getOrNull() }
    }

    /** When the list was last fetched, in milliseconds since the epoch, or null if never. */
    val fetchedAt: Flow<Long?> = metadataDao.observe(key).map { it?.fetchedAt }

    suspend fun isStale(maxAge: Duration): Boolean {
        val fetchedAt = metadataDao.get(key)?.fetchedAt ?: return true
        return clock.now() - fetchedAt >= maxAge.inWholeMilliseconds
    }

    /**
     * Fetches the list and replaces what is stored, unless it is younger than [maxAge] and
     * [force] is false. Returns [ApiResult.Success] when the stored list is current, or
     * [ApiResult.Failure] when the fetch failed; the stored list is kept either way.
     */
    suspend fun refresh(
        maxAge: Duration,
        force: Boolean = false,
        fetch: suspend () -> List<T>
    ): ApiResult<Unit> = mutex.withLock {
        if (!force && !isStale(maxAge)) return@withLock ApiResult.Success(Unit)
        try {
            replace(fetch())
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

    suspend fun replace(items: List<T>) {
        itemDao.replaceList(
            key,
            items.mapIndexed { position, item ->
                CachedItemEntity(
                    listKey = key,
                    itemId = id(item),
                    position = position,
                    json = json.encodeToString(serializer, item)
                )
            }
        )
        metadataDao.upsert(FetchMetadataEntity(key = key, fetchedAt = clock.now()))
    }
}
