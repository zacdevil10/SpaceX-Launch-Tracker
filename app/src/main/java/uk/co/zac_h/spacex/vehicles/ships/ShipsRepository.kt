package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.datasource.remote.ShipDataSourceClient
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.network.dto.spacex.ShipQueriedResponse
import javax.inject.Inject

class ShipsRepository @Inject constructor(
    @ShipDataSourceClient shipDataSource: RemoteDataSource<NetworkDocsResponse<ShipQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<ShipQueriedResponse>>
) : Repository<NetworkDocsResponse<ShipQueriedResponse>>(shipDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location
}
