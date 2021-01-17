package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchesPresenterImpl(
    private val view: NetworkInterface.View<List<Launch>>,
    private val interactor: NetworkInterface.Interactor<List<Launch>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Launch>?> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.showProgress()
        interactor.get(data, api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Launch>?) {
        response?.let {
            view.apply {
                hideProgress()
                update(it)
                toggleSwipeRefresh(false)
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