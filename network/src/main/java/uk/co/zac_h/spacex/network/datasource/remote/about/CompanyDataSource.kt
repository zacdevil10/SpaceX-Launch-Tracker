package uk.co.zac_h.spacex.network.datasource.remote.about

import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.CompanyResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.SpaceXHttpClientV4
import uk.co.zac_h.spacex.network.retrofit.SpaceXService
import javax.inject.Inject

class CompanyDataSource @Inject constructor(
    @SpaceXHttpClientV4 private val httpService: SpaceXService
) : RemoteDataSource<CompanyResponse> {

    private suspend fun getCompany() = httpService.getCompanyInfo()

    override suspend fun fetchAsync(query: QueryModel) = getCompany()
}
