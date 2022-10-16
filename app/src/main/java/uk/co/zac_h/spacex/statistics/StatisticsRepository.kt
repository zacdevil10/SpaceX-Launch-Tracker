package uk.co.zac_h.spacex.statistics

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.LaunchesDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.LegacyLaunchQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    @LaunchesDataSourceClient launchesDataSourceClient: RemoteDataSource<NetworkDocsResponse<LegacyLaunchQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LegacyLaunchQueriedResponse>>
) : Repository<NetworkDocsResponse<LegacyLaunchQueriedResponse>>(launchesDataSourceClient, cache) {

    val cacheLocation: RequestLocation
        get() = location
}
