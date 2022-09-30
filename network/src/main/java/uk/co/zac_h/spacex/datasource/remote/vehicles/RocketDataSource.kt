package uk.co.zac_h.spacex.datasource.remote.vehicles

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.RocketResponse
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class RocketDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<MutableList<RocketResponse>> {

    private suspend fun getRockets() = httpService.getRockets()

    override suspend fun fetchAsync(query: QueryModel) = getRockets()
}
