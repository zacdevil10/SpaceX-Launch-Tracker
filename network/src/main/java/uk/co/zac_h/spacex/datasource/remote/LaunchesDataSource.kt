package uk.co.zac_h.spacex.datasource.remote

import uk.co.zac_h.spacex.dto.spacex.LaunchQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV5
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class LaunchesDataSource @Inject constructor(
    @SpaceXHttpClientV5 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<LaunchQueriedResponse>> {

    private suspend fun getLaunches(query: QueryModel) = httpService.queryLaunches(query)

    override suspend fun fetchAsync(query: QueryModel) = getLaunches(query)
}
