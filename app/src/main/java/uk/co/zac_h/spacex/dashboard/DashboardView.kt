package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.LaunchesModel

interface DashboardView {

    fun setLaunchesList(launches: LinkedHashMap<String, LaunchesModel>)

    fun setPinnedList(pinned: ArrayList<LaunchesModel>)

    fun updateLaunchesList()

    fun updatePinnedList()

    fun updateCountdown(countdown: String)

    fun setCountdown(launchDateUnix: Long)

    fun showPinnedHeading()

    fun hidePinnedHeading()

    fun showProgress()

    fun hideProgress()

    fun showCountdown()

    fun hideCountdown()

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)
}