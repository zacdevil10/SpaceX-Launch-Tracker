package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.spacex.CompanyModel

interface CompanyView {

    fun updateCompanyInfo(companyModel: CompanyModel)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}