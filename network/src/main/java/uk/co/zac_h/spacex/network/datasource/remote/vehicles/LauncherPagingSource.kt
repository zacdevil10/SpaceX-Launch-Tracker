package uk.co.zac_h.spacex.network.datasource.remote.vehicles

import uk.co.zac_h.spacex.network.datasource.SpaceXPagingSource
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailed
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class LauncherPagingSource(
    private val httpService: LaunchLibraryService
) : SpaceXPagingSource<LauncherDetailed>() {

    override suspend fun getResponse(
        limit: Int,
        offset: Int
    ) = httpService.getLaunchers(
        limit = limit,
        offset = offset
    )
}
