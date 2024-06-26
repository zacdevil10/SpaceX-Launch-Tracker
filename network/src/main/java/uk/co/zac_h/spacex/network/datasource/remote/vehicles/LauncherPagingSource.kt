package uk.co.zac_h.spacex.network.datasource.remote.vehicles

import uk.co.zac_h.spacex.network.datasource.SpaceXPagingSource
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class LauncherPagingSource(
    private val httpService: LaunchLibraryService
) : SpaceXPagingSource<LauncherResponse>() {

    override suspend fun getResponse(
        limit: Int,
        offset: Int
    ) = httpService.getLaunchers(
        limit = limit,
        offset = offset
    )
}
