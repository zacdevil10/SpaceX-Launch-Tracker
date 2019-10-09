package uk.co.zac_h.spacex.about

import uk.co.zac_h.spacex.model.CompanyModel

interface CompanyView {

    fun updateCompanyInfo(companyModel: CompanyModel)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}