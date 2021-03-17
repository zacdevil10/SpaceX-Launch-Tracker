package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.rest.SpaceXInterface

class ShipsPresenterImpl(
    private val view: NetworkInterface.View<List<Ship>>,
    private val interactor: NetworkInterface.Interactor<List<Ship>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Ship>?> {

    override fun get(api: SpaceXInterface) {
        interactor.get(api, this)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(response: List<Ship>?) {
        response?.let {
            view.update(response)
            view.toggleSwipeRefresh(false)
        }
    }

    override fun onError(error: String) {
        view.toggleSwipeRefresh(false)
        view.showError(error)
    }
}