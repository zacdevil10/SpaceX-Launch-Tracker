package uk.co.zac_h.spacex.base

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface NetworkInterface {

    interface View<T> {
        fun update(response: T) {}
        fun update(data: Any, response: T) {}
        fun showProgress() {}
        fun hideProgress() {}
        fun showError(error: String) {}
        fun toggleSwipeRefresh(isRefreshing: Boolean) {}
    }

    interface Presenter<T> {
        fun get(api: SpaceXInterface = SpaceXInterface.create()) {}
        fun get(data: Any, api: SpaceXInterface = SpaceXInterface.create()) {}
        fun getOrUpdate(response: T, api: SpaceXInterface = SpaceXInterface.create()) {}
        fun cancelRequest()
    }

    interface Interactor<T> {
        fun get(api: SpaceXInterface, listener: Callback<T>) {}
        fun get(data: Any, api: SpaceXInterface, listener: Callback<T>) {}
        fun cancelAllRequests()
    }

    interface Callback<T> {
        fun onSuccess(response: T) {}
        fun onSuccess(data: Any, response: T) {}
        fun onError(error: String)
    }

}