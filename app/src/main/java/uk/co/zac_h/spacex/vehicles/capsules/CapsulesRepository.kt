package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.CapsuleDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CapsulesDocsModel
import uk.co.zac_h.spacex.utils.OrderSharedPreferences
import javax.inject.Inject

class CapsulesRepository @Inject constructor(
    @CapsuleDataSourceClient capsuleDataSource: RemoteDataSource<CapsulesDocsModel>,
    cache: Cache<CapsulesDocsModel>,
    private val sharedPreferences: OrderSharedPreferences
) : Repository<CapsulesDocsModel>(capsuleDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

    fun getOrder(): Boolean = sharedPreferences.isSortedNew("capsules")

    fun setOrder(order: Boolean) = sharedPreferences.setSortOrder("capsules", order)

}