package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.LaunchesModel

interface DashboardView {

    fun setLaunchesList(launches: LinkedHashMap<String, LaunchesModel>)

    fun updateLaunchesList()

    fun updatePinnedList(pinned: LinkedHashMap<String, LaunchesModel>)

    fun updateCountdown(countdown: String)

    fun setCountdown(launchDateUnix: Long)

    fun showPinnedMessage()

    fun hidePinnedMessage()

    fun showProgress()

    fun hideProgress()

    fun showCountdown()

    fun hideCountdown()

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)
}