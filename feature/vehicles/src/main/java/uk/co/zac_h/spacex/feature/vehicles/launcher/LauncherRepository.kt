package uk.co.zac_h.spacex.feature.vehicles.launcher

import uk.co.zac_h.spacex.network.datasource.remote.vehicles.LauncherPagingSource
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import javax.inject.Inject

class LauncherRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService
) {

    val launcherPagingSource: LauncherPagingSource
        get() = LauncherPagingSource(httpService)
}
