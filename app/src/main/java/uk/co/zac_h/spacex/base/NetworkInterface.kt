package uk.co.zac_h.spacex.base

import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

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
        fun get(api: SpaceXService = NetworkModule.providesSpaceXHttpClientV4()) {}
        fun get(data: Any, api: SpaceXService = NetworkModule.providesSpaceXHttpClientV4()) {}
        fun getOrUpdate(response: T, api: SpaceXService = NetworkModule.providesSpaceXHttpClientV4()) {
            get(api)
        }
        fun getOrUpdate(response: T, data: Any, api: SpaceXService = NetworkModule.providesSpaceXHttpClientV4()) {
            get(data, api)
        }
        fun cancelRequest()
    }

    interface Interactor<T> {
        fun get(api: SpaceXService, listener: Callback<T>) {}
        fun get(data: Any, api: SpaceXService, listener: Callback<T>) {}
        fun cancelAllRequests()
    }

    interface Callback<T> {
        fun onSuccess(response: T) {}
        fun onSuccess(data: Any, response: T) {}
        fun onError(error: String)
    }

}