package uk.co.zac_h.spacex.launches.details.cores

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.LaunchCore
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL_V5

class LaunchDetailsCoresPresenter(
    private val view: NetworkInterface.View<List<LaunchCore>>,
    private val interactor: NetworkInterface.Interactor<List<LaunchCore>>
) : NetworkInterface.Presenter<List<LaunchCore>>, NetworkInterface.Callback<List<LaunchCore>> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.toggleSwipeRefresh(true)
        interactor.get(data, SpaceXInterface.create(SPACEX_BASE_URL_V5), this)
    }

    override fun getOrUpdate(response: List<LaunchCore>, data: Any, api: SpaceXInterface) {
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