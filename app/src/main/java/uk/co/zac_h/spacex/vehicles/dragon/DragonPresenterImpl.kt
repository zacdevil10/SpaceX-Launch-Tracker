package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Dragon
import uk.co.zac_h.spacex.retrofit.SpaceXService

class DragonPresenterImpl(
    private val view: NetworkInterface.View<List<Dragon>>,
    private val interactor: NetworkInterface.Interactor<List<Dragon>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Dragon>?> {

    override fun get(api: SpaceXService) {
        interactor.get(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Dragon>?) {
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