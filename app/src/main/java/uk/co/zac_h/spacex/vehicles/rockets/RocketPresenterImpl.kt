package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.RocketsModel

class RocketPresenterImpl(private val view: RocketView, private val interactor: RocketInteractor) :
    RocketPresenter, RocketInteractor.Callback {

    override fun getRockets() {
        view.showProgress()
        interactor.getRockets(this)
    }

    override fun onSuccess(rockets: List<RocketsModel>?) {
        rockets?.let { view.updateRockets(it.reversed()) }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.error(error)
    }
}