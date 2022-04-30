package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.CapsuleDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CapsulesDocsModel
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import javax.inject.Inject

class CapsulesRepository @Inject constructor(
    @CapsuleDataSourceClient capsuleDataSource: RemoteDataSource<CapsulesDocsModel>,
    cache: Cache<CapsulesDocsModel>,
    private val sharedPreferences: OrderSharedPreferences
) : Repository<CapsulesDocsModel>(capsuleDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

    fun getOrder(): Order = if (sharedPreferences.isSortedNew("capsules")) {
        Order.ASCENDING
    } else {
        Order.DESCENDING
    }

    fun setOrder(order: Order) = sharedPreferences.setSortOrder(
        "capsules",
        when (order) {
            Order.ASCENDING -> true
            Order.DESCENDING -> false
        }
    )

}