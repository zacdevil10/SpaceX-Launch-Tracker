package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CapsulesContract {

    interface CapsulesView {
        fun updateCapsules(capsules: List<CapsulesModel>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showError(error: String)
    }

    interface CapsulesPresenter {
        fun getCapsules(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequests()
    }

    interface CapsulesInteractor {
        fun getCapsules(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(capsules: List<CapsulesModel>?)
        fun onError(error: String)
    }
}