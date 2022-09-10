package uk.co.zac_h.spacex.datasource.remote.vehicles

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.ShipQueriedResponse
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class ShipDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<ShipQueriedResponse>> {

    private suspend fun getShips(query: QueryModel) = httpService.queryShips(query)

    override suspend fun fetchAsync(query: QueryModel) = getShips(query)

}