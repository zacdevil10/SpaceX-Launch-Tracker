package uk.co.zac_h.spacex.datasource.remote

import uk.co.zac_h.spacex.dto.spacex.LaunchDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV5
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class LaunchesDataSource @Inject constructor(
    @SpaceXHttpClientV5 private val httpService: SpaceXService
) : RemoteDataSource<LaunchDocsModel> {

    private suspend fun getLaunches(query: QueryModel) = httpService.queryLaunches(query)

    override suspend fun fetchAsync(query: QueryModel) = getLaunches(query)

}