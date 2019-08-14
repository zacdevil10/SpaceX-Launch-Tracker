package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchesPresenterImpl(private val view: LaunchesView, private val interactor: LaunchesInteractor) : LaunchesPresenter, LaunchesInteractor.InteractorCallback {

    override fun getLaunchList(id: String) {
        interactor.getLaunches(id, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.updateLaunchesList(launches)
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}