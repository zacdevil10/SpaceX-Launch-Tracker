package uk.co.zac_h.spacex.datasource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Response
import uk.co.zac_h.spacex.dto.spacex.HistoryDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class HistoryDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<HistoryDocsModel> {

    private suspend fun getHistory(body: QueryModel) = httpService.queryHistory(body)

    override suspend fun fetchAsync(body: QueryModel) = getHistory(body)

}