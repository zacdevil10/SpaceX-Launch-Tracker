package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface DragonContract {

    interface DragonView {
        fun updateDragon(dragon: List<DragonModel>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showError(error: String)
    }

    interface DragonPresenter {
        fun getDragon(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface DragonInteractor {
        fun getDragon(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(dragon: List<DragonModel>?)
        fun onError(error: String)
    }
}