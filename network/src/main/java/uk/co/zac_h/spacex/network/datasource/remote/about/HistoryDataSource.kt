package uk.co.zac_h.spacex.network.datasource.remote.about

import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.HistoryResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.network.retrofit.SpaceXService
import javax.inject.Inject

class HistoryDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<HistoryResponse>> {

    private suspend fun getHistory(body: QueryModel) = httpService.queryHistory(body)

    override suspend fun fetchAsync(query: QueryModel) = getHistory(query)
}
