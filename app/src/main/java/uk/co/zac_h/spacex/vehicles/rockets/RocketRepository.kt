package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.datasource.remote.RocketDataSourceClient
import uk.co.zac_h.spacex.network.dto.spacex.RocketResponse
import javax.inject.Inject

class RocketRepository @Inject constructor(
    @RocketDataSourceClient rocketDataSource: RemoteDataSource<List<RocketResponse>>,
    cache: Cache<List<RocketResponse>>
) : Repository<List<RocketResponse>>(rocketDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location
}
