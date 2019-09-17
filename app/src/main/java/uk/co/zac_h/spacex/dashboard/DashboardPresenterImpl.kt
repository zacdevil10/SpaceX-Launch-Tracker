package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.DashboardListModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class DashboardPresenterImpl(
    private val view: DashboardView,
    private val prefs: PinnedSharedPreferencesHelper,
    private val interactor: DashboardInteractor
) : DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    private val launchesMap = ArrayList<ArrayList<DashboardListModel>>()

    private val next = ArrayList<DashboardListModel>()
    private val latest = ArrayList<DashboardListModel>()
    private val pinned = ArrayList<DashboardListModel>()

    private var completeNext: Boolean = false
    private var completeLatest: Boolean = false
    private var completePinned: Boolean = false

    init {
        next.add(DashboardListModel(null, true, "Next Launch"))
        latest.add(DashboardListModel(null, true, "Latest Launch"))
        pinned.add(DashboardListModel(null, true, "Pinned Launches"))

        launchesMap.add(next)
        launchesMap.add(latest)
        launchesMap.add(pinned)
    }

    override fun getLatestLaunches() {
        view.showProgress()
        interactor.apply {
            getSingleLaunch("next", this@DashboardPresenterImpl)
            getSingleLaunch("latest", this@DashboardPresenterImpl)

            prefs.getAllPinnedLaunches()?.forEach {
                if (it.value as Boolean) getSingleLaunch(it.key, this@DashboardPresenterImpl)
            }
        }
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(id: String, launchesModel: LaunchesModel?) {
        launchesModel?.let {
            when (id) {
                "next" -> {
                    next.add(DashboardListModel(launchesModel, false, null))
                    completeNext = true
                }
                "latest" -> {
                    latest.add(DashboardListModel(launchesModel, false, null))
                    completeLatest = true
                }
                else -> {
                    pinned.add(DashboardListModel(launchesModel, false, null))
                    completePinned = true
                }
            }
        }

        if (completeNext && completeLatest && completePinned) {
            view.updateLaunchesList(launchesMap)
            view.hideProgress()
            view.toggleSwipeProgress(false)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeProgress(false)
    }
}