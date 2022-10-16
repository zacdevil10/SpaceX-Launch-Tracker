package uk.co.zac_h.spacex.network.datasource.remote

import uk.co.zac_h.spacex.network.dto.spacex.CrewQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.network.retrofit.SpaceXService
import javax.inject.Inject

class CrewDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<NetworkDocsResponse<CrewQueriedResponse>> {

    override suspend fun fetchAsync(query: QueryModel) = httpService.queryCrewMembers(query)
}
