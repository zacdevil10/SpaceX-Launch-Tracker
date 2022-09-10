package uk.co.zac_h.spacex.datasource.remote.about

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.HistoryResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class HistoryDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<HistoryResponse>> {

    private suspend fun getHistory(body: QueryModel) = httpService.queryHistory(body)

    override suspend fun fetchAsync(query: QueryModel) = getHistory(query)

}