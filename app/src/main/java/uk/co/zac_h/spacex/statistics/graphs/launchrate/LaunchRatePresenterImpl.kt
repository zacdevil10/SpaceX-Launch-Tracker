package uk.co.zac_h.spacex.statistics.graphs.launchrate

import android.view.View
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchRatePresenterImpl(private val view: LaunchRateView, private val interactor: LaunchRateInteractor) : LaunchRatePresenter,
    LaunchRateInteractor.InteractorCallback {

    override fun getLaunchList() {
        view.toggleProgress(View.VISIBLE)
        interactor.getLaunches(this)
    }

    override fun updateFilter(id: String, state: Boolean) {
        view.updateLaunchesList(id, state)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.toggleProgress(View.GONE)
        view.setLaunchesList(launches)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}