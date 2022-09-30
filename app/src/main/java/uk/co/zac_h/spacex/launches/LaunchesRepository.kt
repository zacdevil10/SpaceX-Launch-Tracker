package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.LaunchesDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LaunchQueriedResponse
import uk.co.zac_h.spacex.dto.spacex.NetworkDocsResponse
import javax.inject.Inject

class LaunchesRepository @Inject constructor(
    @LaunchesDataSourceClient launchesDataSource: RemoteDataSource<NetworkDocsResponse<LaunchQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<LaunchQueriedResponse>>
) : Repository<NetworkDocsResponse<LaunchQueriedResponse>>(launchesDataSource, cache)
