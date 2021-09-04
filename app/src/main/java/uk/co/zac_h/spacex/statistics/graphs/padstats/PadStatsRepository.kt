package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.LandingPadDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.LaunchpadDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LandingPadDocsModel
import uk.co.zac_h.spacex.dto.spacex.LaunchpadDocsModel
import javax.inject.Inject

class LaunchpadRepository @Inject constructor(
    @LaunchpadDataSourceClient launchpadDataSourceClient: RemoteDataSource<LaunchpadDocsModel>,
    cache: Cache<LaunchpadDocsModel>
) : Repository<LaunchpadDocsModel>(launchpadDataSourceClient, cache)

class LandingPadRepository @Inject constructor(
    @LandingPadDataSourceClient landingPadDataSourceClient: RemoteDataSource<LandingPadDocsModel>,
    cache: Cache<LandingPadDocsModel>
) : Repository<LandingPadDocsModel>(landingPadDataSourceClient, cache)