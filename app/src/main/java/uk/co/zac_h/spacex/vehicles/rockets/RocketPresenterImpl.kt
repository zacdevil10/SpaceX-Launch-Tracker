package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.spacex.RocketsModel

class RocketPresenterImpl(private val view: RocketView, private val interactor: RocketInteractor) :
    RocketPresenter, RocketInteractor.Callback {

    override fun getRockets() {
        view.showProgress()
        interactor.getRockets(this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(rockets: List<RocketsModel>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            rockets?.let { updateRockets(it.reversed()) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}