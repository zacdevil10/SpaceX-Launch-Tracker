package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

class LaunchesPresenterImpl(
    private val view: NetworkInterface.View<List<Launch>>,
    private val interactor: NetworkInterface.Interactor<List<Launch>>
) : NetworkInterface.Presenter<List<Launch>>, NetworkInterface.Callback<List<Launch>> {

    override fun get(data: Any, api: SpaceXService) {
        view.showProgress()
        interactor.get(data, NetworkModule.providesSpaceXHttpClientV5(), this)
    }

    override fun getOrUpdate(response: List<Launch>, data: Any, api: SpaceXService) {
        if (response.isNotEmpty()) onSuccess(response) else super.getOrUpdate(response, data, api)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Launch>) {
        view.apply {
            hideProgress()
            update(response)
            toggleSwipeRefresh(false)
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }

}