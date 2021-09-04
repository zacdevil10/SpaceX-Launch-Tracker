package uk.co.zac_h.spacex.datasource.remote.statistics

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LandingPadDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class LandingPadDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<LandingPadDocsModel> {

    private suspend fun getLandingPads(body: QueryModel) = httpService.queryLandingPads(body)

    override suspend fun fetchAsync(query: QueryModel) = getLandingPads(query)

}