package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.database.LaunchDao
import uk.co.zac_h.spacex.core.database.LaunchEntity
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import uk.co.zac_h.spacex.network.retrofit.networkJson
import java.time.Instant
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Launches, with the database as the source of truth. Each launch is stored once; screens observe
 * the database and the refresh functions fill it from Launch Library only when it is out of date.
 */
@Singleton
class LaunchStore @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService,
    private val launchDao: LaunchDao,
    private val refresher: Refresher,
    private val clock: Clock
) {

    /** Upcoming launches, soonest first. Launches that no longer decode are skipped. */
    val upcomingLaunches: Flow<List<LaunchDetailedDto>> =
        launchDao.observeUpcoming().map { entities -> entities.mapNotNull(::decode) }

    /** When upcoming launches were last fetched, in milliseconds since the epoch. */
    val upcomingFetchedAt: Flow<Long?> = refresher.fetchedAt(UPCOMING_LAUNCHES)

    /** Fetches upcoming launches if the stored ones are older than their window, or if [force]. */
    suspend fun refreshUpcoming(force: Boolean = false): ApiResult<Unit> =
        refresher.refresh(UPCOMING_LAUNCHES, Freshness.UPCOMING_LAUNCHES, force) {
            storeUpcoming(httpService.getUpcomingLaunches().results)
        }

    /**
     * Stores the upcoming endpoint's response. The endpoint also returns launches for 24 hours
     * after they fly, so each launch's list comes from its status: one still in flight stays in
     * upcoming, and one with a result moves to previous.
     */
    internal suspend fun storeUpcoming(launches: List<LaunchDetailedDto>) {
        launchDao.replaceUpcoming(
            launches.map { it.toEntity(upcoming = it.isUpcoming(fromUpcomingEndpoint = true)) },
            clock.now()
        )
    }

    private fun decode(entity: LaunchEntity): LaunchDetailedDto? =
        runCatching { networkJson.decodeFromString(LaunchDetailedDto.serializer(), entity.json) }.getOrNull()

    companion object {
        internal const val UPCOMING_LAUNCHES = "launches_upcoming"
    }
}

/**
 * Launch Library status ids for a launch without a result yet: Go (1), TBD (2), Hold (5),
 * In Flight (6) and TBC (8). Success (3), Failure (4) and Partial Failure (7) are results.
 */
private val UPCOMING_STATUS_IDS = setOf(1, 2, 5, 6, 8)

/**
 * Whether a launch belongs in upcoming: until it has a result, including while in flight. With no
 * status, falls back to which endpoint it came from.
 */
internal fun LaunchDetailedDto.isUpcoming(fromUpcomingEndpoint: Boolean): Boolean =
    status?.id?.let { it in UPCOMING_STATUS_IDS } ?: fromUpcomingEndpoint

internal fun LaunchDetailedDto.toEntity(upcoming: Boolean) = LaunchEntity(
    id = id,
    net = net.toEpochMillis(),
    upcoming = upcoming,
    lastUpdated = lastUpdated.toEpochMillis(),
    json = networkJson.encodeToString(LaunchDetailedDto.serializer(), this)
)

private fun String?.toEpochMillis(): Long? = this?.let {
    runCatching { Instant.parse(it).toEpochMilli() }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(it).toInstant().toEpochMilli() }.getOrNull()
}
