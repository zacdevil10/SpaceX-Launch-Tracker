package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class RocketPresenterImpl(private val view: RocketView, private val interactor: RocketInteractor) :
    RocketPresenter, RocketInteractor.Callback {

    override fun getRockets(api: SpaceXInterface) {
        view.showProgress()
        interactor.getRockets(api, this)
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