package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchRatePresenterImpl(private val view: LaunchRateView, private val interactor: LaunchRateInteractor) : LaunchRatePresenter,
    LaunchRateInteractor.InteractorCallback {

    override fun getLaunchList() {
        interactor.getLaunches(this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.setLaunchesList(launches)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}