package uk.co.zac_h.spacex.launches.details.crew

import uk.co.zac_h.spacex.crew.CrewContract
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsCrewPresenter(
    private val view: CrewContract.CrewView,
    private val interactor: LaunchDetailsCrewContract.Interactor
) : LaunchDetailsCrewContract.Presenter, LaunchDetailsCrewContract.InteractorCallback {

    override fun getCrew(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getCrew(id, api, this)
    }

    override fun cancelRequest() = interactor.cancelRequest()

    override fun onSuccess(crew: List<Crew>?) {
        view.hideProgress()
        crew?.let {
            view.updateCrew(it)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}