package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.LaunchesModel

class LaunchesPresenterImpl(
    private val view: LaunchesView,
    private val interactor: LaunchesInteractor
) : LaunchesPresenter, LaunchesInteractor.InteractorCallback {

    override fun getLaunchList(id: String, order: String) {
        view.showProgress()
        interactor.getLaunches(id, order, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.hideProgress()
        view.updateLaunchesList(launches)
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeProgress(false)
    }

}