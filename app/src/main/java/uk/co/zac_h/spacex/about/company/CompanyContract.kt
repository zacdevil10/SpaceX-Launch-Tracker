package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CompanyContract {

    interface CompanyView {
        fun updateCompanyInfo(company: Company)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface CompanyPresenter {
        fun getCompanyInfo(
            companyInfo: Company? = null,
            api: SpaceXInterface = SpaceXInterface.create()
        )

        fun cancelRequest()
    }

    interface CompanyInteractor {
        fun getCompanyInfo(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(company: Company?)
        fun onError(error: String)
    }
}