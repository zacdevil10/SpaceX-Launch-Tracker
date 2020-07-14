package uk.co.zac_h.spacex.launches.details.crew

import uk.co.zac_h.spacex.crew.CrewContract
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
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

    override fun onSuccess(launchesExtendedDocsModel: LaunchesExtendedDocsModel?) {
        if (launchesExtendedDocsModel?.docs?.isNotEmpty() == true) {
            launchesExtendedDocsModel.docs[0].crew?.let {
                view.updateCrew(it)
            }
        }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}