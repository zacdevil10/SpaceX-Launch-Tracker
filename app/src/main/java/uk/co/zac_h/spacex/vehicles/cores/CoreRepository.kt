package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.CoreDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CoreQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import javax.inject.Inject

class CoreRepository @Inject constructor(
    @CoreDataSourceClient coreDataSource: RemoteDataSource<NetworkDocsResponse<CoreQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<CoreQueriedResponse>>,
    private val sharedPreferences: OrderSharedPreferences
) : Repository<NetworkDocsResponse<CoreQueriedResponse>>(coreDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

    fun getOrder(): Order = if (sharedPreferences.isSortedNew("cores")) {
        Order.ASCENDING
    } else {
        Order.DESCENDING
    }

    fun setOrder(order: Order) = sharedPreferences.setSortOrder(
        "cores",
        when (order) {
            Order.ASCENDING -> true
            Order.DESCENDING -> false
        }
    )
}
