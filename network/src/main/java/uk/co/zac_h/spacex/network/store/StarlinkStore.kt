package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.core.database.StarlinkDao
import uk.co.zac_h.spacex.core.database.StarlinkSatelliteEntity
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CELESTRAK_GROUP_STARLINK
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
    private val refresher: Refresher
) {

    val satellites: Flow<List<StarlinkSatelliteEntity>> = dao.observeAll()

    /** When the elements were last downloaded, in milliseconds since the epoch. */
    val fetchedAt: Flow<Long?> = refresher.fetchedAt(STARLINK)

    suspend fun refresh(force: Boolean = false): ApiResult<Unit> =
        refresher.refresh(STARLINK, Freshness.STARLINK, force) {
            val satellites = service.getGroup(CELESTRAK_GROUP_STARLINK).mapNotNull { it.toEntity() }
            // An empty answer would wipe the globe; keep what is stored instead.
            check(satellites.isNotEmpty()) { "CelesTrak returned no Starlink satellites" }
            dao.replaceAll(satellites)
        }

    companion object {
        internal const val STARLINK = "starlink_elements"
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
