package uk.co.zac_h.spacex.network.datasource.remote

import retrofit2.Response
import uk.co.zac_h.spacex.network.datasource.SpaceXPagingSource
import uk.co.zac_h.spacex.network.dto.spacex.AstronautResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class AstronautPagingSource(
    private val httpService: LaunchLibraryService
) : SpaceXPagingSource<AstronautResponse>() {

    override suspend fun getResponse(
        limit: Int,
        offset: Int
    ): Response<LaunchLibraryPaginatedResponse<AstronautResponse>> = httpService.getAstronauts(
        limit = limit,
        offset = offset
    )
}
