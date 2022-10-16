package uk.co.zac_h.spacex.network.datasource.remote

import uk.co.zac_h.spacex.network.dto.spacex.LegacyLaunchQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.SpaceXHttpClientV5
import uk.co.zac_h.spacex.network.retrofit.SpaceXService
import javax.inject.Inject

class LaunchesDataSource @Inject constructor(
    @SpaceXHttpClientV5 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<LegacyLaunchQueriedResponse>> {

    private suspend fun getLaunches(query: QueryModel) = httpService.queryLaunches(query)

    override suspend fun fetchAsync(query: QueryModel) = getLaunches(query)
}
