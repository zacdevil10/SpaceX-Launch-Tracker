package uk.co.zac_h.spacex.launches.details.ships

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL_V5

class LaunchDetailsShipsPresenter(
    private val view: NetworkInterface.View<List<Ship>>,
    private val interactor: NetworkInterface.Interactor<List<Ship>>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Ship>> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.toggleSwipeRefresh(true)
        interactor.get(data, SpaceXInterface.create(SPACEX_BASE_URL_V5), this)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(response: List<Ship>) {
        view.update(response)
        view.toggleSwipeRefresh(false)
    }

    override fun onError(error: String) {
        view.apply {
            toggleSwipeRefresh(false)
            showError(error)
        }
    }

}