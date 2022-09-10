package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.LandingPadDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.LaunchpadDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LandingPadQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.LaunchpadQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import javax.inject.Inject

class LaunchpadRepository @Inject constructor(
    @LaunchpadDataSourceClient launchpadDataSourceClient: RemoteDataSource<NetworkDocsResponse<LaunchpadQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LaunchpadQueriedResponse>>
) : Repository<NetworkDocsResponse<LaunchpadQueriedResponse>>(launchpadDataSourceClient, cache)

class LandingPadRepository @Inject constructor(
    @LandingPadDataSourceClient landingPadDataSourceClient: RemoteDataSource<NetworkDocsResponse<LandingPadQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LandingPadQueriedResponse>>
) : Repository<NetworkDocsResponse<LandingPadQueriedResponse>>(landingPadDataSourceClient, cache)