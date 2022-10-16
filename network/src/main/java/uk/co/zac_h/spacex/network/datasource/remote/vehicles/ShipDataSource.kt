package uk.co.zac_h.spacex.network.datasource.remote.vehicles

import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.dto.spacex.ShipQueriedResponse
import uk.co.zac_h.spacex.network.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.network.retrofit.SpaceXService
import javax.inject.Inject

class ShipDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<ShipQueriedResponse>> {

    private suspend fun getShips(query: QueryModel) = httpService.queryShips(query)

    override suspend fun fetchAsync(query: QueryModel) = getShips(query)
}
