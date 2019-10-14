package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.CapsulesModel

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
        view.hideProgress()
        capsules?.let { view.updateCapsules(it) }
    }

    override fun onError(error: String) {
        view.error(error)
    }
}