package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.spacex.CompanyModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CompanyPresenter {

    fun getCompanyInfo(
        companyInfo: CompanyModel? = null,
        api: SpaceXInterface = SpaceXInterface.create()
    )

    fun cancelRequest()

}