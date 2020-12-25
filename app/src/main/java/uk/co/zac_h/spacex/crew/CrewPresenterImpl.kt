package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CrewPresenterImpl(
    private val view: CrewContract.CrewView,
    private val interactor: CrewContract.CrewInteractor
) : CrewContract.CrewPresenter, CrewContract.InteractorCallback {

    override fun getCrew(api: SpaceXInterface) {
        view.showProgress()
        interactor.getCrew(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(crew: List<Crew>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            crew?.let { updateCrew(it) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }

}