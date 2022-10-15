package uk.co.zac_h.spacex.statistics

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.LaunchesDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LegacyLaunchQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    @LaunchesDataSourceClient launchesDataSourceClient: RemoteDataSource<NetworkDocsResponse<LegacyLaunchQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LegacyLaunchQueriedResponse>>
) : Repository<NetworkDocsResponse<LegacyLaunchQueriedResponse>>(launchesDataSourceClient, cache) {

    val cacheLocation: RequestLocation
        get() = location
}
