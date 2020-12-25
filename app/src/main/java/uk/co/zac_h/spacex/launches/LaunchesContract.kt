package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchesContract {

    interface LaunchesView {
        fun updateLaunchesList(launches: List<Launch>?)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeProgress(isRefreshing: Boolean)
        fun showError(error: String)
    }

    interface LaunchesPresenter {
        fun getLaunchList(id: String, api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequests()
    }

    interface LaunchesInteractor {
        fun getLaunches(
            id: String,
            order: String,
            api: SpaceXInterface,
            listener: InteractorCallback
        )

        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(launches: List<Launch>?)
        fun onError(error: String)
    }

}