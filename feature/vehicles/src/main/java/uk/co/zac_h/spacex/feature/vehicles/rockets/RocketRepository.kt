package uk.co.zac_h.spacex.feature.vehicles.rockets

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.AgencyDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import javax.inject.Inject

class RocketRepository @Inject constructor(
    @AgencyDataSourceClient agencyDataSource: RemoteDataSource<AgencyResponse>,
    cache: Cache<AgencyResponse>
) : Repository<AgencyResponse>(agencyDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

    var launcherConfigId: Int? = null
}
