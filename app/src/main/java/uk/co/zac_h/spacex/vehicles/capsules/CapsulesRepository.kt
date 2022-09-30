package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.CapsuleDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CapsuleQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import javax.inject.Inject

class CapsulesRepository @Inject constructor(
    @CapsuleDataSourceClient capsuleDataSource: RemoteDataSource<NetworkDocsResponse<CapsuleQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<CapsuleQueriedResponse>>,
    private val sharedPreferences: OrderSharedPreferences
) : Repository<NetworkDocsResponse<CapsuleQueriedResponse>>(capsuleDataSource, cache) {

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
