package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.model.CompanyModel

class CompanyPresenterImpl(
    private val view: CompanyView,
    private val interactor: CompanyInteractor
) : CompanyPresenter,
    CompanyInteractor.Callback {

    override fun getCompanyInfo() {
        view.showProgress()
        interactor.getCompanyInfo(this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(companyModel: CompanyModel?) {
        view.hideProgress()
        companyModel?.let { view.updateCompanyInfo(it) }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}