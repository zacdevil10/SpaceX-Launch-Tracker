package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import java.util.concurrent.TimeUnit

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

    override fun updateCountdown(time: Long) {
        val remaining = String.format(
            "T-%02d:%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toDays(time),
            TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(
                TimeUnit.MILLISECONDS.toDays(
                    time
                )
            ),
            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    time
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    time
                )
            )
        )

        view.updateCountdown(remaining)
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

        if (launchesMap.size == 2) {
            launchesMap["next"]?.let { launch ->
                launch.tbd?.let {
                    if (!it) {
                        view.apply {
                            setCountdown(launch.launchDateUnix)
                            showCountdown()
                        }
                    } else {
                        view.hideCountdown()
                    }
                }
            }
            view.updateLaunchesList()
        }
        if (!interactor.hasActiveRequest()) view.hideProgress()
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }
}