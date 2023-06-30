package uk.co.zac_h.spacex.network.datasource.remote.launches

import retrofit2.Response
import uk.co.zac_h.spacex.network.datasource.SpaceXPagingSource
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class LaunchesPreviousPagingSource(
    private val httpService: LaunchLibraryService
) : SpaceXPagingSource<LaunchResponse>() {

    override suspend fun getResponse(
        limit: Int,
        offset: Int
    ): Response<LaunchLibraryPaginatedResponse<LaunchResponse>> = httpService.getPreviousLaunches(
        limit = limit,
        offset = offset
    )
}