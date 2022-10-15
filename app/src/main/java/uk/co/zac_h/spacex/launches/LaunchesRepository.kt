package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.datasource.remote.launches.LaunchesPreviousPagingSource
import uk.co.zac_h.spacex.datasource.remote.launches.LaunchesUpcomingPagingSource
import uk.co.zac_h.spacex.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.retrofit.LaunchLibraryService
import javax.inject.Inject

class LaunchesRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService
) {

    val upcomingLaunchesPagingSource: LaunchesUpcomingPagingSource
        get() = LaunchesUpcomingPagingSource(httpService)

    val previousLaunchesPagingSource: LaunchesPreviousPagingSource
        get() = LaunchesPreviousPagingSource(httpService)
}
