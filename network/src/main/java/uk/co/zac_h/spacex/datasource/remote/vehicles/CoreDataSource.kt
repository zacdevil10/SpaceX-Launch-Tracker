package uk.co.zac_h.spacex.datasource.remote.vehicles

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CoreDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class CoreDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<CoreDocsModel> {

    private suspend fun getCores(query: QueryModel) = httpService.queryCores(query)

    override suspend fun fetchAsync(query: QueryModel) = getCores(query)

}