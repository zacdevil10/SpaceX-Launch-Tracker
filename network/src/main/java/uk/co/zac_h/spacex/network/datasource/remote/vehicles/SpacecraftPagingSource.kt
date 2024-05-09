package uk.co.zac_h.spacex.network.datasource.remote.vehicles

import uk.co.zac_h.spacex.network.datasource.SpaceXPagingSource
import uk.co.zac_h.spacex.network.dto.spacex.SpacecraftResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class SpacecraftPagingSource(
    private val httpService: LaunchLibraryService
) : SpaceXPagingSource<SpacecraftResponse>() {

    override suspend fun getResponse(
        limit: Int,
        offset: Int
    ) = httpService.getSpacecrafts(
        limit = limit,
        offset = offset
    )
}
