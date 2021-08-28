package uk.co.zac_h.spacex.launches.details.cores

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.LaunchCore
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

class LaunchDetailsCoresPresenter(
    private val view: NetworkInterface.View<List<LaunchCore>>,
    private val interactor: NetworkInterface.Interactor<List<LaunchCore>>
) : NetworkInterface.Presenter<List<LaunchCore>>, NetworkInterface.Callback<List<LaunchCore>> {

    override fun get(data: Any, api: SpaceXService) {
        view.showProgress()
        interactor.get(data, NetworkModule.providesSpaceXHttpClientV5(), this)
    }

    override fun getOrUpdate(response: List<LaunchCore>, data: Any, api: SpaceXService) {
        if (response.isEmpty()) {
            super.getOrUpdate(response, data, api)
        } else {
            onSuccess(response)
        }
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<LaunchCore>) {
        view.apply {
            update(response)
            hideProgress()
            toggleSwipeRefresh(false)
        }
    }

    override fun onError(error: String) {
        view.apply {
            toggleSwipeRefresh(false)
            showError(error)
        }
    }
}