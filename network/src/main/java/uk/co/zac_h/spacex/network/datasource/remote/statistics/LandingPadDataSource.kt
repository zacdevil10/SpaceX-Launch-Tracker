package uk.co.zac_h.spacex.network.datasource.remote.statistics

import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.LandingPadQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.network.retrofit.SpaceXService
import javax.inject.Inject

class LandingPadDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<LandingPadQueriedResponse>> {

    private suspend fun getLandingPads(body: QueryModel) = httpService.queryLandingPads(body)

    override suspend fun fetchAsync(query: QueryModel) = getLandingPads(query)
}
