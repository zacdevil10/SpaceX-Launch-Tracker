package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.LandingPadDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.LaunchpadDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.LandingPadQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchpadQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import javax.inject.Inject

class LaunchpadRepository @Inject constructor(
    @LaunchpadDataSourceClient launchpadDataSourceClient: RemoteDataSource<NetworkDocsResponse<LaunchpadQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LaunchpadQueriedResponse>>
) : Repository<NetworkDocsResponse<LaunchpadQueriedResponse>>(launchpadDataSourceClient, cache)

class LandingPadRepository @Inject constructor(
    @LandingPadDataSourceClient landingPadDataSourceClient: RemoteDataSource<NetworkDocsResponse<LandingPadQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LandingPadQueriedResponse>>
) : Repository<NetworkDocsResponse<LandingPadQueriedResponse>>(landingPadDataSourceClient, cache)
