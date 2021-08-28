package uk.co.zac_h.spacex.launches.details.ships

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Ship
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

class LaunchDetailsShipsPresenter(
    private val view: NetworkInterface.View<List<Ship>>,
    private val interactor: NetworkInterface.Interactor<List<Ship>>
) : NetworkInterface.Presenter<List<Ship>>, NetworkInterface.Callback<List<Ship>> {

    override fun get(data: Any, api: SpaceXService) {
        view.showProgress()
        interactor.get(data, NetworkModule.providesSpaceXHttpClientV5(), this)
    }

    override fun getOrUpdate(response: List<Ship>, data: Any, api: SpaceXService) {
        if (response.isEmpty()) super.getOrUpdate(response, data, api) else onSuccess(response)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(response: List<Ship>) {
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