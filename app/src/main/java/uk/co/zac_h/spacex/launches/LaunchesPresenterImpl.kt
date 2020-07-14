package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchesPresenterImpl(
    private val view: LaunchesContract.LaunchesView,
    private val interactor: LaunchesContract.LaunchesInteractor
) : LaunchesContract.LaunchesPresenter, LaunchesContract.InteractorCallback {

    override fun getLaunchList(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getLaunches(id, if (id == "past") "desc" else "asc", api, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: LaunchesExtendedDocsModel?) {
        view.apply {
            hideProgress()
            updateLaunchesList(launches?.docs)
            toggleSwipeProgress(false)
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }

}