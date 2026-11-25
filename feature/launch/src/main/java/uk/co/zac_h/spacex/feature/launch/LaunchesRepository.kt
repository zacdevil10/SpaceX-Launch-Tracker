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

class LaunchesRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService,
    @SpaceflightNewsClient private val newsHttpService: SpaceflightNewsService,
    private val launchStore: LaunchStore
) {

    /** Upcoming launches from the database, updated whenever [refreshUpcoming] stores new ones. */
    val upcomingLaunches: Flow<List<LaunchDetailedDto>> = launchStore.upcomingLaunches

    /** When upcoming launches were last fetched, in milliseconds since the epoch. */
    val upcomingFetchedAt: Flow<Long?> = launchStore.upcomingFetchedAt

    /** Fetches upcoming launches if the stored ones are older than their window, or if [force]. */
    suspend fun refreshUpcoming(force: Boolean = false): ApiResult<Unit> =
        launchStore.refreshUpcoming(force)

    val previousLaunchesPagingSource: LaunchesPreviousPagingSource
        get() = LaunchesPreviousPagingSource(httpService)

    fun articlesPagingSource(launchId: String?) = SpaceflightNewsPagingSource(
        httpService = newsHttpService,
        launch = launchId
    )
}
