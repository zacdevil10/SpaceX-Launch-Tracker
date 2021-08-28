package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Rocket
import uk.co.zac_h.spacex.retrofit.SpaceXService

class RocketPresenterImpl(
    private val view: NetworkInterface.View<List<Rocket>>,
    private val interactor: NetworkInterface.Interactor<List<Rocket>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Rocket>?> {

    override fun get(api: SpaceXService) {
        interactor.get(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Rocket>?) {
        view.apply {
            toggleSwipeRefresh(false)
            response?.let { update(it.reversed()) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}