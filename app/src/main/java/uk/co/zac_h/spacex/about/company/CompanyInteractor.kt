package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.spacex.CompanyModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CompanyInteractor {

    fun getCompanyInfo(api: SpaceXInterface, listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(companyModel: CompanyModel?)

        fun onError(error: String)
    }
}