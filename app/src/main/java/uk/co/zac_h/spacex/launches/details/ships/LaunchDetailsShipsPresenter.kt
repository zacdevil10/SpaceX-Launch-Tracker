package uk.co.zac_h.spacex.launches.details.ships

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsShipsPresenter(
    private val view: NetworkInterface.View<List<Ship>>,
    private val interactor: NetworkInterface.Interactor<Launch?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<Launch?> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.showProgress()
        interactor.get(data, api, this)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(response: Launch?) {
        response?.ships?.let {
            view.update(it)
            view.hideProgress()
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}