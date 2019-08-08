package uk.co.zac_h.spacex.dashboard

import android.view.View
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class DashboardPresenterImpl(private val view: DashboardView, private val interactor: DashboardInteractor): DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    override fun getSingleLaunch(id: String) {
        view.toggleProgress(View.VISIBLE)
        interactor.getSingleLaunch(id, this)
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