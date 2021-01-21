package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Core
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CorePresenterImpl(
    private val view: NetworkInterface.View<List<Core>>,
    private val interactor: NetworkInterface.Interactor<List<Core>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Core>?> {

    override fun get(api: SpaceXInterface) {
        view.showProgress()
        interactor.get(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Core>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            response?.let { update(it) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}