package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.spacex.CompanyModel

interface CompanyPresenter {

    fun getCompanyInfo(companyInfo: CompanyModel? = null)

    fun cancelRequest()

}