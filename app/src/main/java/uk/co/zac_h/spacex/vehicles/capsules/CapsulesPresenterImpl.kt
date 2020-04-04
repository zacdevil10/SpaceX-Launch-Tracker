package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.CapsulesModel

class CapsulesPresenterImpl(
    private val view: CapsulesView,
    private val interactor: CapsulesInteractor
) : CapsulesPresenter, CapsulesInteractor.Callback {

    override fun getCapsules() {
        view.showProgress()
        interactor.getCapsules(this)
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