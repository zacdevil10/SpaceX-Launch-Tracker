package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface DashboardContract {

    interface DashboardView {
        fun updateNextLaunch(nextLaunch: LaunchesExtendedModel)
        fun updateLatestLaunch(latestLaunch: LaunchesExtendedModel)
        fun updatePinnedList(id: String, pinnedLaunch: LaunchesExtendedModel)
        fun updateCountdown(countdown: String)
        fun setCountdown(time: Long)
        fun showPinnedMessage()
        fun hidePinnedMessage()
        fun showProgress()
        fun hideProgress()
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
            next: LaunchesExtendedModel? = null,
            latest: LaunchesExtendedModel? = null,
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
        fun onSuccess(id: String, launchModel: LaunchesExtendedModel?)
        fun onError(error: String)
    }
}