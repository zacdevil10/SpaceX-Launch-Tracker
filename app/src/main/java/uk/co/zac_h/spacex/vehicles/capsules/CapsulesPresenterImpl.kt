package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Capsule
import uk.co.zac_h.spacex.retrofit.SpaceXService

class CapsulesPresenterImpl(
    private val view: NetworkInterface.View<List<Capsule>>,
    private val interactor: NetworkInterface.Interactor<List<Capsule>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Capsule>?> {

    override fun get(api: SpaceXService) {
        interactor.get(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Capsule>?) {
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