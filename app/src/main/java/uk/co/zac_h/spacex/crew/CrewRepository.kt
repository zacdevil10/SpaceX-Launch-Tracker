package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.CrewDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.CrewQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import javax.inject.Inject

class CrewRepository @Inject constructor(
    @CrewDataSourceClient crewDataSource: RemoteDataSource<NetworkDocsResponse<CrewQueriedResponse>>,
    cache: Cache<NetworkDocsResponse<CrewQueriedResponse>>
) : Repository<NetworkDocsResponse<CrewQueriedResponse>>(crewDataSource, cache) {

    val cacheLocation: RequestLocation
        get() = location
}
