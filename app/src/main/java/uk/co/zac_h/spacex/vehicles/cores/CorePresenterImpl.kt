package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.spacex.CoreDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CorePresenterImpl(
    private val view: CoreContract.CoreView,
    private val interactor: CoreContract.CoreInteractor
) : CoreContract.CorePresenter, CoreContract.InteractorCallback {

    override fun getCores(api: SpaceXInterface) {
        view.showProgress()
        interactor.getCores(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(cores: CoreDocsModel?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            cores?.let { updateCores(it.docs) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}