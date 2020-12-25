package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface DashboardContract {

    interface DashboardView {
        fun updateNextLaunch(nextLaunch: Launch)
        fun updateLatestLaunch(latestLaunch: Launch)
        fun updatePinnedList(id: String, pinnedLaunch: Launch)
        fun updateCountdown(countdown: String)
        fun setCountdown(time: Long)
        fun showPinnedMessage()
        fun hidePinnedMessage()
        fun toggleNextProgress(isShown: Boolean): Unit?
        fun toggleLatestProgress(isShown: Boolean): Unit?
        fun togglePinnedProgress(isShown: Boolean): Unit?
        fun showCountdown()
        fun hideCountdown()
        fun showNextLaunch()
        fun hideNextLaunch()
        fun showLatestLaunch()
        fun hideLatestLaunch()
        fun showPinnedList()
        fun hidePinnedList()
        fun toggleSwipeProgress(isRefreshing: Boolean)
        fun showError(error: String)
    }

    interface DashboardPresenter {
        fun getLatestLaunches(
            next: Launch? = null,
            latest: Launch? = null,
            api: SpaceXInterface = SpaceXInterface.create()
        )

        fun getSingleLaunch(
            id: String,
            api: SpaceXInterface = SpaceXInterface.create()
        )

        fun updateCountdown(time: Long)
        fun cancelRequests()
        fun toggleNextLaunchVisibility(visible: Boolean)
        fun toggleLatestLaunchVisibility(visible: Boolean)
        fun togglePinnedList(visible: Boolean)
    }

    interface DashboardInteractor {
        fun getSingleLaunch(id: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests()
    }

    interface InteractorCallback {
        fun onSuccess(id: String, launchModel: Launch?)
        fun onError(error: String)
    }
}