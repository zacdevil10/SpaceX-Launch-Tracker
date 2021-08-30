package uk.co.zac_h.spacex.datasource.remote.dashboard

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LaunchDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV5
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class DashboardDataSource @Inject constructor(
    @SpaceXHttpClientV5 private val httpService: SpaceXService
) : RemoteDataSource<LaunchDocsModel> {

    private suspend fun getLaunch(query: QueryModel) = httpService.queryLaunches(query)

    override suspend fun fetchAsync(query: QueryModel) = getLaunch(query)

}