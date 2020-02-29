package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CapsulesPresenterImpl(
    private val view: CapsulesView,
    private val interactor: CapsulesInteractor
) : CapsulesPresenter, CapsulesInteractor.Callback {

    override fun getCapsules(api: SpaceXInterface) {
        view.showProgress()
        interactor.getCapsules(api, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(capsules: List<CapsulesModel>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            capsules?.let { updateCapsules(it) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}