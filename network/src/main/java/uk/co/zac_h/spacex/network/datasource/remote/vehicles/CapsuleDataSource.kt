package uk.co.zac_h.spacex.network.datasource.remote.vehicles

import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.CapsuleQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.network.retrofit.SpaceXService
import javax.inject.Inject

class CapsuleDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<CapsuleQueriedResponse>> {

    private suspend fun getCores(query: QueryModel) = httpService.queryCapsules(query)

    override suspend fun fetchAsync(query: QueryModel) = getCores(query)
}
