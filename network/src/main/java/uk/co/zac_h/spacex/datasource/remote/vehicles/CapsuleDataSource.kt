package uk.co.zac_h.spacex.datasource.remote.vehicles

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CapsulesDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class CapsuleDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<CapsulesDocsModel> {

    private suspend fun getCores(query: QueryModel) = httpService.queryCapsules(query)

    override suspend fun fetchAsync(query: QueryModel) = getCores(query)

}