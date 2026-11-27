package uk.co.zac_h.spacex.core.data

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.data.mapper.toLaunch
import uk.co.zac_h.spacex.core.model.Launch
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.store.Freshness
import uk.co.zac_h.spacex.network.store.LaunchStore
import javax.inject.Inject
import kotlin.time.Duration

/**
 * Launches for every form factor, from the Room cache that [LaunchStore] keeps current. Screens
 * observe the flows and call the refresh functions; nothing here returns a Launch Library DTO.
 */
class LaunchRepository @Inject constructor(
    private val launchStore: LaunchStore
) {

    /** Upcoming launches, soonest first, updated whenever [refreshUpcoming] stores new ones. */
    val upcomingLaunches: Flow<List<Launch>> =
        launchStore.upcomingLaunches.map { launches -> launches.map { it.toLaunch() } }

    /** One stored launch, upcoming or previous, or null while it isn't stored. */
    fun launch(id: String): Flow<Launch?> = launchStore.launch(id).map { it?.toLaunch() }

    /** Previous launches, newest first, fetching only older pages and changes as needed. */
    fun previousLaunches(): Flow<PagingData<Launch>> =
        launchStore.previousLaunches().map { page -> page.map { it.toLaunch() } }

    /** When upcoming launches were last fetched, in milliseconds since the epoch. */
    val upcomingFetchedAt: Flow<Long?> = launchStore.upcomingFetchedAt

    /** When the next launch was last fetched, in milliseconds since the epoch. */
    val nextLaunchFetchedAt: Flow<Long?> = launchStore.nextLaunchFetchedAt

    /** How long a manual refresh of the next launch is ignored after the last one. */
    val manualRefreshCooldown: Duration = Freshness.MANUAL_REFRESH_COOLDOWN

    /** Fetches upcoming launches if the stored ones are older than their window, or if [force]. */
    suspend fun refreshUpcoming(force: Boolean = false): ApiResult<Unit> =
        launchStore.refreshUpcoming(force)

    /** Fetches the next launch if it is more than a minute old, or if [force]. */
    suspend fun refreshNextLaunch(force: Boolean = false): ApiResult<Unit> =
        launchStore.refreshNextLaunch(force)

    /** The automatic next launch refresh; null when skipped to save calls. */
    suspend fun pollNextLaunch(): ApiResult<Unit>? = launchStore.pollNextLaunch()

    /** How long to wait before the next automatic refresh of the next launch. */
    suspend fun nextLaunchPollInterval(): Duration = launchStore.nextLaunchPollInterval()
}
