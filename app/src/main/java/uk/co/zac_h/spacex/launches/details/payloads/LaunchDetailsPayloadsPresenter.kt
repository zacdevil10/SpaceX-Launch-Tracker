package uk.co.zac_h.spacex.launches.details.payloads

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.Payload
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsPayloadsPresenter(
    private val view: NetworkInterface.View<List<Payload>>,
    private val interactor: NetworkInterface.Interactor<Launch?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<Launch?> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.showProgress()
        interactor.get(data, api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: Launch?) {
        response?.payloads?.let { payloads ->
            view.update(payloads)
            view.hideProgress()
        }

    }

    override fun onError(error: String) {
        view.showError(error)
    }
}