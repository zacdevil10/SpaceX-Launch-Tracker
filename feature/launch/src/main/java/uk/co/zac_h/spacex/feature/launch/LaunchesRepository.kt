package uk.co.zac_h.spacex.feature.launch

import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.datasource.remote.launches.LaunchesPreviousPagingSource
import uk.co.zac_h.spacex.network.datasource.remote.news.SpaceflightNewsPagingSource
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsClient
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsService
import uk.co.zac_h.spacex.network.store.LaunchStore
import javax.inject.Inject
import kotlin.time.Duration

class LaunchesRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService,
    @SpaceflightNewsClient private val newsHttpService: SpaceflightNewsService,
    private val launchStore: LaunchStore
) {

    /** Upcoming launches from the database, updated whenever [refreshUpcoming] stores new ones. */
    val upcomingLaunches: Flow<List<LaunchDetailedDto>> = launchStore.upcomingLaunches

    /** When upcoming launches were last fetched, in milliseconds since the epoch. */
    val upcomingFetchedAt: Flow<Long?> = launchStore.upcomingFetchedAt

    /** When the next launch was last fetched, in milliseconds since the epoch. */
    val nextLaunchFetchedAt: Flow<Long?> = launchStore.nextLaunchFetchedAt

    /** Fetches upcoming launches if the stored ones are older than their window, or if [force]. */
    suspend fun refreshUpcoming(force: Boolean = false): ApiResult<Unit> =
        launchStore.refreshUpcoming(force)

    /** Fetches the next launch if it is more than a minute old, or if [force]. */
    suspend fun refreshNextLaunch(force: Boolean = false): ApiResult<Unit> =
        launchStore.refreshNextLaunch(force)

    /** The automatic next launch refresh; null when skipped to save calls. */
    suspend fun pollNextLaunch(): ApiResult<Unit>? = launchStore.pollNextLaunch()

    suspend fun nextLaunchPollInterval(): Duration = launchStore.nextLaunchPollInterval()

    val previousLaunchesPagingSource: LaunchesPreviousPagingSource
        get() = LaunchesPreviousPagingSource(httpService)

    fun articlesPagingSource(launchId: String?) = SpaceflightNewsPagingSource(
        httpService = newsHttpService,
        launch = launchId
    )
}
