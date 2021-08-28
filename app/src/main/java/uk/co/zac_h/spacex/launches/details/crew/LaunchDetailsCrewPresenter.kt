package uk.co.zac_h.spacex.launches.details.crew

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Crew
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

class LaunchDetailsCrewPresenter(
    private val view: NetworkInterface.View<List<Crew>>,
    private val interactor: NetworkInterface.Interactor<List<Crew>>
) : NetworkInterface.Presenter<List<Crew>>, NetworkInterface.Callback<List<Crew>> {

    override fun get(data: Any, api: SpaceXService) {
        view.toggleSwipeRefresh(true)
        interactor.get(data, NetworkModule.providesSpaceXHttpClientV5(), this)
    }

    override fun getOrUpdate(response: List<Crew>, data: Any, api: SpaceXService) {
        if (response.isEmpty()) super.getOrUpdate(response, data, api) else onSuccess(response)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(response: List<Crew>) {
        view.update(response)
        view.toggleSwipeRefresh(false)
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeRefresh(false)
    }
}