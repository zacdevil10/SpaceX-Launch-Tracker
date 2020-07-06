package uk.co.zac_h.spacex.vehicles

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface VehiclesContract {

    interface View<T> {
        fun updateVehicles(vehicles: List<T>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showError(error: String)
    }

    interface Presenter {
        fun getVehicles(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface Interactor<T> {
        fun getVehicles(api: SpaceXInterface, listener: InteractorCallback<T>)
        fun cancelAllRequests()
    }

    interface InteractorCallback<T> {
        fun onSuccess(vehicles: List<T>?)
        fun onError(error: String)
    }

}