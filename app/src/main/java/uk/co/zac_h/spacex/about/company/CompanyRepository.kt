package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.SpaceXHttpClient
import uk.co.zac_h.spacex.rest.SpaceXInterface
import javax.inject.Inject
import javax.inject.Singleton

class CompanyRepository @Inject constructor(
    @SpaceXHttpClient private val httpService: SpaceXInterface
) {

    suspend fun getCompany() = httpService.getCompanyInfo()

}