package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.datasource.remote.ShipDataSourceClient
import uk.co.zac_h.spacex.dto.spacex.ShipsDocsModel
import javax.inject.Inject

class ShipsRepository @Inject constructor(
    @ShipDataSourceClient shipDataSource: RemoteDataSource<ShipsDocsModel>,
    cache: Cache<ShipsDocsModel>
) : Repository<ShipsDocsModel>(shipDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

}