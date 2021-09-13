package uk.co.zac_h.spacex.datasource.remote

import uk.co.zac_h.spacex.dto.spacex.CrewDocsModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.retrofit.SpaceXService
import javax.inject.Inject

class CrewDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<CrewDocsModel> {

    override suspend fun fetchAsync(query: QueryModel) = httpService.queryCrewMembers(query)

}