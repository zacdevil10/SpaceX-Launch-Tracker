package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.SpaceXHttpClient
import uk.co.zac_h.spacex.model.spacex.QueryModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.Keys
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    @SpaceXHttpClient private val httpService: SpaceXInterface,
    private val sharedPreferences: OrderSharedPreferences
) {

    suspend fun getHistory(body: QueryModel) = httpService.queryHistory(body)

    fun getOrder(): Boolean = sharedPreferences.isSortedNew(Keys.HistoryKeys.HISTORY_ORDER)

    fun setOrder(order: Boolean) =
        sharedPreferences.setSortOrder(Keys.HistoryKeys.HISTORY_ORDER, order)

}