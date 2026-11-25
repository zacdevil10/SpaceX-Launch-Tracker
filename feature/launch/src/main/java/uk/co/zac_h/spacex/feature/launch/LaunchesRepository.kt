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
import uk.co.zac_h.spacex.network.store.Freshness
import uk.co.zac_h.spacex.network.store.LaunchLibraryStore
import javax.inject.Inject

class LaunchesRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService,
    @SpaceflightNewsClient private val newsHttpService: SpaceflightNewsService,
    store: LaunchLibraryStore
) {

    private val upcoming = store.upcomingLaunches

    /** Upcoming launches from the database, updated whenever [refreshUpcoming] stores new ones. */
    val upcomingLaunches: Flow<List<LaunchDetailedDto>> = upcoming.items

    /** When upcoming launches were last fetched, in milliseconds since the epoch. */
    val upcomingFetchedAt: Flow<Long?> = upcoming.fetchedAt

    /** Fetches upcoming launches if the stored ones are older than their window, or if [force]. */
    suspend fun refreshUpcoming(force: Boolean = false): ApiResult<Unit> =
        upcoming.refresh(maxAge = Freshness.UPCOMING_LAUNCHES, force = force) {
            httpService.getUpcomingLaunches().results
        }

    val previousLaunchesPagingSource: LaunchesPreviousPagingSource
        get() = LaunchesPreviousPagingSource(httpService)

    fun articlesPagingSource(launchId: String?) = SpaceflightNewsPagingSource(
        httpService = newsHttpService,
        launch = launchId
    )
}
