package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.CoreModel

class CorePresenterImpl(private val view: CoreView, private val interactor: CoreInteractor) :
    CorePresenter, CoreInteractor.Callback {

    override fun getCores() {
        view.showProgress()
        interactor.getCores(this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(cores: List<CoreModel>?) {
        view.hideProgress()
        cores?.let { view.updateCores(it) }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}