package uk.co.zac_h.spacex.launches.details.crew

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL_V5

class LaunchDetailsCrewPresenter(
    private val view: NetworkInterface.View<List<Crew>>,
    private val interactor: NetworkInterface.Interactor<List<Crew>>
) : NetworkInterface.Presenter<List<Crew>>, NetworkInterface.Callback<List<Crew>> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.toggleSwipeRefresh(true)
        interactor.get(data, api, this)
    }

    override fun getOrUpdate(response: List<Crew>, data: Any, api: SpaceXInterface) {
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