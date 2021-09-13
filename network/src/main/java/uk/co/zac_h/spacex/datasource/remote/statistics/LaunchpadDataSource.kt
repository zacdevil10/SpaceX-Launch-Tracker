package uk.co.zac_h.spacex.datasource.remote.statistics

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LaunchpadDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class LaunchpadDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<LaunchpadDocsModel> {

    private suspend fun getLaunchpads(body: QueryModel) = httpService.queryLaunchpads(body)

    override suspend fun fetchAsync(query: QueryModel) = getLaunchpads(query)

}