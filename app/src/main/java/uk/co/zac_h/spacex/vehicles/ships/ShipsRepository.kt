package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.datasource.remote.ShipDataSourceClient
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.dto.spacex.ShipQueriedResponse
import javax.inject.Inject

class ShipsRepository @Inject constructor(
    @ShipDataSourceClient shipDataSource: RemoteDataSource<NetworkDocsResponse<ShipQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<ShipQueriedResponse>>
) : Repository<NetworkDocsResponse<ShipQueriedResponse>>(shipDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

}