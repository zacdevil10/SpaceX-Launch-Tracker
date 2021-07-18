package uk.co.zac_h.spacex.launches.details.crew

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.crew.CrewView
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL_V5

class LaunchDetailsCrewPresenter(
    private val view: CrewView,
    private val interactor: NetworkInterface.Interactor<List<Crew>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<Crew>?> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.showProgress()
        interactor.get(data, SpaceXInterface.create(SPACEX_BASE_URL_V5), this)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(response: List<Crew>?) {
        response?.let {
            view.update(it)
            view.hideProgress()
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}