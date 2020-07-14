package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.model.spacex.CrewDocsModel
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

    override fun onSuccess(crew: CrewDocsModel?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            crew?.let { updateCrew(it.docs) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }

}