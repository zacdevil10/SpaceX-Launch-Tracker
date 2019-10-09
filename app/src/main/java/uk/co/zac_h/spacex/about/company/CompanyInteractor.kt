package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.CompanyModel

interface CompanyInteractor {

    fun getCompanyInfo(listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(companyModel: CompanyModel?)

        fun onError(error: String)
    }
}