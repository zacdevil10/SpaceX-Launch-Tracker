package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

class DashboardPresenterImpl(private val view: DashboardView, private val interactor: DashboardInteractor): DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    override fun getSingleLaunch(id: String) {
        interactor.getSingleLaunch(id, this)
    }

    override fun onSuccess(id: String, launchesModel: LaunchesModel?) {
        view.updateLaunchesList(id, launchesModel)
    }

    override fun onError(error: String) {

    }
}