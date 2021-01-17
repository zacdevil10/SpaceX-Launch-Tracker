package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.util.concurrent.TimeUnit

class DashboardPresenterImpl(
    private val view: DashboardContract.View,
    private val interactor: NetworkInterface.Interactor<Launch?>
) : DashboardContract.Presenter,
    NetworkInterface.Callback<Launch?> {

    override fun getLatestLaunches(
        next: Launch?,
        latest: Launch?,
        api: SpaceXInterface
    ) {
        interactor.apply {
            if (next == null) {
                view.toggleNextProgress(true)
                get("next", api, this@DashboardPresenterImpl)
            } else onSuccess("next", next)

            if (latest == null) {
                view.toggleLatestProgress(true)
                get("latest", api, this@DashboardPresenterImpl)
            } else onSuccess("latest", latest)
        }
    }

    override fun get(data: Any, api: SpaceXInterface) {
        view.togglePinnedProgress(true)
        interactor.get(data, api, this)
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

    override fun cancelRequest() {
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

    override fun onSuccess(data: Any, response: Launch?) {
        response?.let { launch ->
            when (data as String) {
                "next" -> {
                    view.toggleNextProgress(false)
                    val time =
                        (launch.launchDate?.dateUnix?.times(1000) ?: 0) - System.currentTimeMillis()
                    if (launch.tbd == false && time >= 0) view.apply {
                        setCountdown(time)
                        showCountdown()
                        hideNextHeading()
                    } else view.apply {
                        hideCountdown()
                        showNextHeading()
                    }
                }
                "latest" -> {
                    view.toggleLatestProgress(false)
                }
                else -> {
                    view.hidePinnedMessage()
                    view.togglePinnedProgress(false)
                }
            }
            view.update(data, launch)
        }
        view.toggleSwipeRefresh(false)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}