package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.remote.CrewDataSourceClient
import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CrewDocsModel
import javax.inject.Inject

class CrewRepository @Inject constructor(
    @CrewDataSourceClient crewDataSource: RemoteDataSource<CrewDocsModel>,
    cache: Cache<CrewDocsModel>
) : Repository<CrewDocsModel>(crewDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location

}