package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

interface DashboardContract {

    interface View : NetworkInterface.View<Launch> {
        fun updateCountdown(countdown: String)
        fun setCountdown(time: Long)
        fun showPinnedMessage()
        fun hidePinnedMessage()
        fun toggleNextProgress(isShown: Boolean): Unit?
        fun toggleLatestProgress(isShown: Boolean): Unit?
        fun togglePinnedProgress(isShown: Boolean): Unit?
        fun showCountdown()
        fun hideCountdown()
        fun showNextHeading()
        fun hideNextHeading()
        fun showNextLaunch()
        fun hideNextLaunch()
        fun showLatestLaunch()
        fun hideLatestLaunch()
        fun showPinnedList()
        fun hidePinnedList()
    }

    interface Presenter : NetworkInterface.Presenter<Launch?> {
        fun getLatestLaunches(
                next: Launch? = null,
                latest: Launch? = null,
                api: SpaceXService = NetworkModule.providesSpaceXHttpClientV4()
        )

        fun updateCountdown(time: Long)
        fun toggleNextVisibility(visible: Boolean)
        fun toggleLatestVisibility(visible: Boolean)
        fun togglePinnedList(visible: Boolean)
    }
}