package uk.co.zac_h.spacex.network.datasource.remote

import uk.co.zac_h.spacex.network.datasource.SpaceXPagingSource
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class AstronautPagingSource(
    private val httpService: LaunchLibraryService
) : SpaceXPagingSource<AstronautEndpointNormalDto>() {

    override suspend fun getResponse(
        limit: Int,
        offset: Int
    ) = httpService.getAstronauts(
        limit = limit,
        offset = offset
    )
}
