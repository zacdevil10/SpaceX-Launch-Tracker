package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import uk.co.zac_h.spacex.core.database.FetchMetadataDao
import uk.co.zac_h.spacex.core.database.FetchMetadataEntity
import uk.co.zac_h.spacex.core.database.StarlinkDao
import uk.co.zac_h.spacex.core.database.StarlinkSatelliteEntity
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CELESTRAK_GROUP_STARLINK
import uk.co.zac_h.spacex.network.CelestrakRateLimitedException
import uk.co.zac_h.spacex.network.celestrakRetryAt
import uk.co.zac_h.spacex.network.dto.celestrak.GpElementDto
import uk.co.zac_h.spacex.network.retrofit.CelestrakService
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Starlink orbital elements, with the database as the source of truth. The whole group is
 * downloaded from CelesTrak at most once per [Freshness.STARLINK].
 */
@Singleton
class StarlinkStore @Inject constructor(
    private val service: CelestrakService,
    private val dao: StarlinkDao,
    private val refresher: Refresher,
    private val metadataDao: FetchMetadataDao,
    private val clock: Clock
) {

    val satellites: Flow<List<StarlinkSatelliteEntity>> = dao.observeAll()

    /** When the elements were last downloaded, in milliseconds since the epoch. */
    val fetchedAt: Flow<Long?> = refresher.fetchedAt(STARLINK)

    /**
     * Downloads the group if the stored elements are older than [Freshness.STARLINK], or if
     * [force]. After CelesTrak refuses a download, nothing is requested again until its wait is
     * over, even if [force]: CelesTrak blocks IPs that keep asking.
     */
    suspend fun refresh(force: Boolean = false): ApiResult<Unit> {
        val retryAt = metadataDao.get(RETRY_AT)?.fetchedAt
        if (retryAt != null && clock.now() < retryAt && refresher.isStale(STARLINK, Freshness.STARLINK)) {
            return ApiResult.Failure(CelestrakRateLimitedException(retryAt))
        }
        return refresher.refresh(STARLINK, Freshness.STARLINK, force) {
            val response = try {
                service.getGroup(CELESTRAK_GROUP_STARLINK)
            } catch (e: HttpException) {
                if (e.code() != 403) throw e
                val body = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
                val wait = celestrakRetryAt(body, clock.now())
                metadataDao.upsert(FetchMetadataEntity(key = RETRY_AT, fetchedAt = wait))
                throw CelestrakRateLimitedException(wait)
            }
            val satellites = response.mapNotNull { it.toEntity() }
            // An empty answer would wipe the globe; keep what is stored instead.
            check(satellites.isNotEmpty()) { "CelesTrak returned no Starlink satellites" }
            dao.replaceAll(satellites)
        }
    }

    companion object {
        internal const val STARLINK = "starlink_elements"

        /** Stores when CelesTrak allows the next download (in fetched_at) after a 403. */
        internal const val RETRY_AT = "starlink_retry_at"
    }
}

/** The entity for this record, or null if its epoch can't be read. */
internal fun GpElementDto.toEntity(): StarlinkSatelliteEntity? {
    val epochMillis = runCatching {
        LocalDateTime.parse(epoch).toInstant(ZoneOffset.UTC).toEpochMilli()
    }.getOrNull() ?: return null
    return StarlinkSatelliteEntity(
        noradId = noradCatId,
        name = objectName,
        epoch = epochMillis,
        meanMotion = meanMotion,
        eccentricity = eccentricity,
        inclination = inclination,
        raOfAscNode = raOfAscNode,
        argOfPericenter = argOfPericenter,
        meanAnomaly = meanAnomaly,
        bstar = bstar
    )
}
