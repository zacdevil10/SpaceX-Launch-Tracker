package uk.co.zac_h.spacex.launches.details.payloads

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Payload
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

class LaunchDetailsPayloadsPresenter(
    private val view: NetworkInterface.View<List<Payload>>,
    private val interactor: NetworkInterface.Interactor<List<Payload>>
) : NetworkInterface.Presenter<List<Payload>>, NetworkInterface.Callback<List<Payload>> {

    override fun get(data: Any, api: SpaceXService) {
        view.showProgress()
        interactor.get(data, NetworkModule.providesSpaceXHttpClientV5(), this)
    }

    override fun getOrUpdate(response: List<Payload>, data: Any, api: SpaceXService) {
        if (response.isEmpty()) super.getOrUpdate(response, data, api) else onSuccess(response)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Payload>) {
        view.apply {
            update(response)
            hideProgress()
            toggleSwipeRefresh(false)
        }
    }

    override fun onError(error: String) {
        view.apply {
            toggleSwipeRefresh(false)
            showError(error)
        }
    }
}