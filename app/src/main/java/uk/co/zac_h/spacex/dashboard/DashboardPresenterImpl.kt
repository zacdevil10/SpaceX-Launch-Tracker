package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.dto.spacex.Upcoming
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.utils.*

class DashboardPresenterImpl(
    private val view: DashboardContract.View,
    private val interactor: NetworkInterface.Interactor<Launch>
) : DashboardContract.Presenter,
    NetworkInterface.Callback<Launch> {

    override fun getLatestLaunches(next: Launch?, latest: Launch?, api: SpaceXService) {
        if (next == null) {
            view.toggleNextProgress(true)
            interactor.get(
                Upcoming.NEXT,
                NetworkModule.providesSpaceXHttpClientV5(),
                this@DashboardPresenterImpl
            )
        } else onSuccess(Upcoming.NEXT, next)

        if (latest == null) {
            view.toggleLatestProgress(true)
            interactor.get(
                Upcoming.LATEST,
                    NetworkModule.providesSpaceXHttpClientV5(),
                this@DashboardPresenterImpl
            )
        } else onSuccess(Upcoming.LATEST, latest)
    }

    override fun get(data: Any, api: SpaceXService) {
        view.togglePinnedProgress(true)
        interactor.get(data, NetworkModule.providesSpaceXHttpClientV5(), this)
    }

    override fun updateCountdown(time: Long) {
        view.updateCountdown(
            String.format(
                "T-%02d:%02d:%02d:%02d",
                time.toCountdownDays(),
                time.toCountdownHours(),
                time.toCountdownMinutes(),
                time.toCountdownSeconds()
            )
        )
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun toggleNextVisibility(visible: Boolean) {
        if (visible) view.showNextLaunch() else view.hideNextLaunch()
    }

    override fun toggleLatestVisibility(visible: Boolean) {
        if (visible) view.showLatestLaunch() else view.hideLatestLaunch()
    }

    override fun togglePinnedList(visible: Boolean) {
        if (visible) view.showPinnedList() else view.hidePinnedList()
    }

    override fun onSuccess(data: Any, response: Launch) {
        when (data) {
            Upcoming.NEXT -> view.apply {
                toggleNextProgress(false)
                val time = response.launchDate?.dateUnix?.times(1000)
                    ?.minus(System.currentTimeMillis()) ?: 0
                if (response.tbd == false && time >= 0) {
                    setCountdown(time)
                    showCountdown()
                    hideNextHeading()
                } else {
                    hideCountdown()
                    showNextHeading()
                }
            }
            Upcoming.LATEST -> view.toggleLatestProgress(false)
            else -> view.apply {
                hidePinnedMessage()
                togglePinnedProgress(false)
            }
        }
        view.update(data, response)
        view.toggleSwipeRefresh(false)
    }

    override fun onError(error: String) {
        view.showError(error)
        view.toggleSwipeRefresh(false)
    }
}