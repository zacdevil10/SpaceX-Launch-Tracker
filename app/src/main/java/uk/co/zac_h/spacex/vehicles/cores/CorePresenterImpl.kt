package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Core
import uk.co.zac_h.spacex.retrofit.SpaceXService

class CorePresenterImpl(
    private val view: NetworkInterface.View<List<Core>>,
    private val interactor: NetworkInterface.Interactor<List<Core>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Core>?> {

    override fun get(api: SpaceXService) {
        interactor.get(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Core>?) {
        response?.let {
            view.apply {
                toggleSwipeRefresh(false)
                update(it)
            }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}