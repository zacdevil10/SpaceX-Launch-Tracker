package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.Upcoming
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL_V5
import java.util.concurrent.TimeUnit

class DashboardPresenterImpl(
    private val view: DashboardContract.View,
    private val interactor: NetworkInterface.Interactor<Launch>
) : DashboardContract.Presenter,
    NetworkInterface.Callback<Launch> {

    override fun getLatestLaunches(
        next: Launch?,
        latest: Launch?,
        api: SpaceXInterface
    ) {
        interactor.apply {
            if (next == null) {
                view.toggleNextProgress(true)
                get(Upcoming.NEXT, SpaceXInterface.create(SPACEX_BASE_URL_V5), this@DashboardPresenterImpl)
            } else onSuccess(Upcoming.NEXT, next)

            if (latest == null) {
                view.toggleLatestProgress(true)
                get(Upcoming.LATEST, SpaceXInterface.create(SPACEX_BASE_URL_V5), this@DashboardPresenterImpl)
            } else onSuccess(Upcoming.LATEST, latest)
        }
    }

    override fun get(data: Any, api: SpaceXInterface) {
        view.togglePinnedProgress(true)
        interactor.get(data, SpaceXInterface.create(SPACEX_BASE_URL_V5), this)
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

    override fun onSuccess(data: Any, response: Launch) {
        when (data) {
            Upcoming.NEXT -> {
                view.toggleNextProgress(false)
                val time =
                    (response.launchDate?.dateUnix?.times(1000) ?: 0) - System.currentTimeMillis()
                if (response.tbd == false && time >= 0) view.apply {
                    setCountdown(time)
                    showCountdown()
                    hideNextHeading()
                } else view.apply {
                    hideCountdown()
                    showNextHeading()
                }
            }
            Upcoming.LATEST -> {
                view.toggleLatestProgress(false)
            }
            else -> {
                view.hidePinnedMessage()
                view.togglePinnedProgress(false)
            }
        }
        view.update(data, response)
        view.toggleSwipeRefresh(false)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}