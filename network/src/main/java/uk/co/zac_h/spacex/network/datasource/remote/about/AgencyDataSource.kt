package uk.co.zac_h.spacex.network.datasource.remote.about

import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import javax.inject.Inject

class AgencyDataSource @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService
) : RemoteDataSource<AgencyResponse> {

    private suspend fun getCompany() = httpService.getAgency()

    override suspend fun fetchAsync(query: QueryModel) = getCompany()
}
