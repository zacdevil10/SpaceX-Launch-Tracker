package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.DashboardListModel

class DashboardPresenterImpl(
    private val view: DashboardView,
    private val interactor: DashboardInteractor
) : DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    private val launchesArray = ArrayList<DashboardListModel>()

    override fun getLatestLaunches() {
        view.showProgress()
        launchesArray.clear()
        interactor.apply {
            getSingleLaunch("next", this@DashboardPresenterImpl)
            getSingleLaunch("latest", this@DashboardPresenterImpl)
        }
    }

    override fun getPinnedLaunch(id: String) {
        interactor.getSingleLaunch(id, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(id: String, launchesModel: LaunchesModel?) {
        launchesModel?.let {
            when (id) {
                "next" -> {
                    launchesArray.add(0, DashboardListModel(launchesModel, false, null))
                    launchesArray.add(0, DashboardListModel(null, true, "Next Launch"))
                }
                "latest" -> {
                    launchesArray.add(DashboardListModel(null, true, "Latest Launch"))
                    launchesArray.add(DashboardListModel(launchesModel, false, null))
                }
                else -> {
                    launchesArray.add(DashboardListModel(null, true, "Pinned Launches"))
                    launchesArray.add(DashboardListModel(launchesModel, false, null))
                }
            }
        }
        if (launchesArray.size > 1) {
            view.updateLaunchesList(launchesArray)
            view.hideProgress()
            view.toggleSwipeProgress(false)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeProgress(false)
    }
}