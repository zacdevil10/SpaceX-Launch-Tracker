package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.LaunchesModel

interface DashboardView {

    fun updateNextLaunch(nextLaunch: LaunchesModel)

    fun updateLatestLaunch(latestLaunch: LaunchesModel)

    fun updatePinnedList(id: String, pinnedLaunch: LaunchesModel)

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