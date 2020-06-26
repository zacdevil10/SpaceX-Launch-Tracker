package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.CapsulesDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CapsulesPresenterImpl(
    private val view: CapsulesContract.CapsulesView,
    private val interactor: CapsulesContract.CapsulesInteractor
) : CapsulesContract.CapsulesPresenter, CapsulesContract.InteractorCallback {

    override fun getCapsules(api: SpaceXInterface) {
        view.showProgress()
        interactor.getCapsules(api, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(capsules: CapsulesDocsModel?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            capsules?.let { updateCapsules(it.docs) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}