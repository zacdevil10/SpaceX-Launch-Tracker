package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.spacex.CompanyModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CompanyContract {

    interface CompanyView {

        fun updateCompanyInfo(companyModel: CompanyModel)

        fun showProgress()

        fun hideProgress()

        fun showError(error: String)

    }

    interface CompanyPresenter {

        fun getCompanyInfo(
            companyInfo: CompanyModel? = null,
            api: SpaceXInterface = SpaceXInterface.create()
        )

        fun cancelRequest()

    }

    interface CompanyInteractor {

        fun getCompanyInfo(api: SpaceXInterface, listener: InteractorCallback)

        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(companyModel: CompanyModel?)

        fun onError(error: String)
    }
}