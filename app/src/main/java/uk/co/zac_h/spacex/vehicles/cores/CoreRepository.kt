package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.CoreDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CoreDocsModel
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import javax.inject.Inject

class CoreRepository @Inject constructor(
    @CoreDataSourceClient coreDataSource: RemoteDataSource<CoreDocsModel>,
    cache: Cache<CoreDocsModel>,
    private val sharedPreferences: OrderSharedPreferences
) : Repository<CoreDocsModel>(coreDataSource, cache) {

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