package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

class DashboardPresenterImpl(private val view: DashboardView, private val interactor: DashboardInteractor): DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    private val launches = LinkedHashMap<String, LaunchesModel>()

    override fun getLatestLaunches() {
        view.showProgress()
        interactor.apply {
            getSingleLaunch("next", this@DashboardPresenterImpl)
            getSingleLaunch("latest", this@DashboardPresenterImpl)
        }
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(id: String, launchesModel: LaunchesModel?) {
        launchesModel?.let {
            launches[id] = it
        }
        if (launches.size > 1) {
            view.updateLaunchesList(id, launches)
            view.hideProgress()
            view.toggleSwipeProgress(false)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeProgress(false)
    }
}