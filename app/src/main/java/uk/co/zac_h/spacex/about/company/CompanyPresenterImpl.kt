package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CompanyPresenterImpl(
    private val view: CompanyContract.CompanyView,
    private val interactor: CompanyContract.CompanyInteractor
) : CompanyContract.CompanyPresenter, CompanyContract.InteractorCallback {

    override fun getCompanyInfo(companyInfo: Company?, api: SpaceXInterface) {
        view.showProgress()
        companyInfo?.let {
            this.onSuccess(it)
        } ?: interactor.getCompanyInfo(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(company: Company?) {
        view.apply {
            hideProgress()
            company?.let {
                updateCompanyInfo(it)
            }
        }

    }

    override fun onError(error: String) {
        view.showError(error)
    }
}