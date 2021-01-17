package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CrewPresenterImpl(
    private val view: CrewView,
    private val interactor: NetworkInterface.Interactor<List<Crew>?>
) : NetworkInterface.Presenter<List<Crew>?>, NetworkInterface.Callback<List<Crew>?> {

    override fun get(api: SpaceXInterface) {
        view.showProgress()
        interactor.get(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<Crew>?) {
        response?.let {
            view.apply {
                hideProgress()
                toggleSwipeRefresh(false)
                update(it)
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