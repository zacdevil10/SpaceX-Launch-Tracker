package uk.co.zac_h.spacex.statistics

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.LaunchesDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.LaunchDocsModel
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    @LaunchesDataSourceClient launchesDataSourceClient: RemoteDataSource<LaunchDocsModel>,
    cache: Cache<LaunchDocsModel>
) : Repository<LaunchDocsModel>(launchesDataSourceClient, cache) {

    val cacheLocation: RequestLocation
        get() = location

}