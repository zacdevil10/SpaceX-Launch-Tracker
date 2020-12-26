package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.util.concurrent.TimeUnit

class DashboardPresenterImpl(
    private val view: DashboardContract.DashboardView,
    private val interactor: DashboardContract.DashboardInteractor
) : DashboardContract.DashboardPresenter,
    DashboardContract.InteractorCallback {

    override fun getLatestLaunches(
        next: Launch?,
        latest: Launch?,
        api: SpaceXInterface
    ) {
        view.showPinnedMessage()

        interactor.apply {
            if (next == null) {
                view.toggleNextProgress(true)
                getSingleLaunch("next", api, this@DashboardPresenterImpl)
            } else onSuccess("next", next)

            if (latest == null) {
                view.toggleLatestProgress(true)
                getSingleLaunch("latest", api, this@DashboardPresenterImpl)
            } else onSuccess("latest", latest)
        }
    }

    override fun getSingleLaunch(id: String, api: SpaceXInterface) {
        view.togglePinnedProgress(true)
        interactor.getSingleLaunch(id, api, this)
    }

    override fun updateCountdown(time: Long) {
        val remaining = String.format(
            "T-%02d:%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toDays(time),
            TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(
                TimeUnit.MILLISECONDS.toDays(time)
            ),
            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(time)
            ),
            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(time)
            )
        )

        view.updateCountdown(remaining)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun toggleNextLaunchVisibility(visible: Boolean) {
        view.apply {
            if (visible) showNextLaunch() else hideNextLaunch()
        }
    }

    override fun toggleLatestLaunchVisibility(visible: Boolean) {
        view.apply {
            if (visible) showLatestLaunch() else hideLatestLaunch()
        }
    }

    override fun togglePinnedList(visible: Boolean) {
        view.apply {
            if (visible) showPinnedList() else hidePinnedList()
        }
    }

    override fun onSuccess(id: String, launchModel: Launch?) {
        launchModel?.let { launch ->
            when (id) {
                "next" -> {
                    view.updateNextLaunch(launch)
                    view.toggleNextProgress(false)
                    val time =
                        (launch.launchDate?.dateUnix?.times(1000) ?: 0) - System.currentTimeMillis()
                    if (launch.tbd == false && time >= 0) view.apply {
                        setCountdown(time)
                        showCountdown()
                    } else view.hideCountdown()
                }
                "latest" -> {
                    view.updateLatestLaunch(launch)
                    view.toggleLatestProgress(false)
                }
                else -> {
                    view.hidePinnedMessage()
                    view.updatePinnedList(id, launch)
                    view.togglePinnedProgress(false)
                }
            }
        }
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }
}