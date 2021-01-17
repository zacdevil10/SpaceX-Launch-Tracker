package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface

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
            api: SpaceXInterface = SpaceXInterface.create()
        )

        fun updateCountdown(time: Long)
        fun toggleNextLaunchVisibility(visible: Boolean)
        fun toggleLatestLaunchVisibility(visible: Boolean)
        fun togglePinnedList(visible: Boolean)
    }
}