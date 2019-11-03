package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.LaunchesModel

class LaunchesPresenterImpl(
    private val view: LaunchesView,
    private val interactor: LaunchesInteractor
) : LaunchesPresenter, LaunchesInteractor.InteractorCallback {

    override fun getLaunchList(id: String) {
        view.showProgress()
        interactor.getLaunches(id, if (id == "past") "desc" else "asc", this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.apply {
            hideProgress()
            updateLaunchesList(launches)
            toggleSwipeProgress(false)
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            hideProgress()
            toggleSwipeProgress(false)
        }
    }

}