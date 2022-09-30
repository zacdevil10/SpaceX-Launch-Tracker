package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.DragonDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.DragonResponse
import javax.inject.Inject

class DragonRepository @Inject constructor(
    @DragonDataSourceClient dragonDataSource: RemoteDataSource<List<DragonResponse>>,
    cache: Cache<List<DragonResponse>>
) : Repository<List<DragonResponse>>(dragonDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location
}
