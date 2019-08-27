package uk.co.zac_h.spacex.dashboard

import android.view.View
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class DashboardPresenterImpl(private val view: DashboardView, private val interactor: DashboardInteractor): DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    override fun getLatestLaunches() {
        view.toggleProgress(View.VISIBLE)
        interactor.apply {
            getSingleLaunch("next", this@DashboardPresenterImpl)
            getSingleLaunch("latest", this@DashboardPresenterImpl)
        }
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(id: String, launchesModel: LaunchesModel?) {
        view.updateLaunchesList(id, launchesModel)
        view.toggleProgress(View.GONE)
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeProgress(false)
    }
}