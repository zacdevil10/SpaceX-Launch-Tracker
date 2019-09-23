package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class DashboardPresenterImpl(
    private val view: DashboardView,
    private val prefs: PinnedSharedPreferencesHelper,
    private val interactor: DashboardInteractor
) : DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    private val launchesMap = LinkedHashMap<String, LaunchesModel>()
    private val pinnedLaunches = ArrayList<LaunchesModel>()

    override fun getLatestLaunches(isRefresh: Boolean) {
        view.showProgress()
        view.hidePinnedHeading()

        if (isRefresh) {
            launchesMap.clear()
            pinnedLaunches.clear()
        }

        if (launchesMap.isEmpty()) {
            interactor.apply {
                getSingleLaunch("next", this@DashboardPresenterImpl)
                getSingleLaunch("latest", this@DashboardPresenterImpl)
            }

            if (!isRefresh) view.setLaunchesList(launchesMap)
        }

        if (pinnedLaunches.isEmpty()) {
            interactor.apply {
                prefs.getAllPinnedLaunches()?.forEach {
                    if (it.value as Boolean) getSingleLaunch(
                        it.key,
                        this@DashboardPresenterImpl
                    )
                }
            }

            if (!isRefresh) view.setPinnedList(pinnedLaunches)
        }
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(id: String, launchesModel: LaunchesModel?) {
        launchesModel?.let {
            when (id) {
                "next", "latest" -> {
                    launchesMap[id] = it
                }
                else -> {
                    pinnedLaunches.add(it)
                    view.updatePinnedList()
                    view.showPinnedHeading()
                }
            }
        }

        view.updateLaunchesList()
        view.hideProgress()
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeProgress(false)
    }
}