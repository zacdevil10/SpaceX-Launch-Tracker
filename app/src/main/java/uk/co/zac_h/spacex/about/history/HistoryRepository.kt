package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.HistoryDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.HistoryResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.Keys
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    @HistoryDataSourceClient historyDataSource: RemoteDataSource<NetworkDocsResponse<HistoryResponse>>,
    cache: Cache<NetworkDocsResponse<HistoryResponse>>,
    private val sharedPreferences: OrderSharedPreferences
) : Repository<NetworkDocsResponse<HistoryResponse>>(historyDataSource, cache) {

    fun getOrder(): Order = if (sharedPreferences.isSortedNew(Keys.HistoryKeys.HISTORY_ORDER)) {
        Order.ASCENDING
    } else {
        Order.DESCENDING
    }

    fun setOrder(order: Order) = sharedPreferences.setSortOrder(
        Keys.HistoryKeys.HISTORY_ORDER,
        when (order) {
            Order.ASCENDING -> true
            Order.DESCENDING -> false
        }
    )
}
