package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface RocketContract {

    interface RocketView {
        fun updateRockets(rockets: List<RocketsModel>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showError(error: String)
    }

    interface RocketPresenter {
        fun getRockets(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface RocketInteractor {
        fun getRockets(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(rockets: List<RocketsModel>?)
        fun onError(error: String)
    }
}