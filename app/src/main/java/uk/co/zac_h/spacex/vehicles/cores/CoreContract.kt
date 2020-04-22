package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CoreContract {

    interface CoreView {
        fun updateCores(cores: List<CoreModel>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showError(error: String)
    }

    interface CorePresenter {
        fun getCores(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface CoreInteractor {
        fun getCores(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(cores: List<CoreModel>?)
        fun onError(error: String)
    }
}