package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.datasource.remote.RocketDataSourceClient
import uk.co.zac_h.spacex.dto.spacex.RocketResponse
import javax.inject.Inject

class RocketRepository @Inject constructor(
    @RocketDataSourceClient rocketDataSource: RemoteDataSource<List<RocketResponse>>,
    cache: Cache<List<RocketResponse>>
) : Repository<List<RocketResponse>>(rocketDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

}