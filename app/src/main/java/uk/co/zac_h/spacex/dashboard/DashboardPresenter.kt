package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface DashboardPresenter {

    fun getLatestLaunches(api: SpaceXInterface = SpaceXInterface.create())

    fun updateCountdown(time: Long)

    fun cancelRequests()

    fun toggleNextLaunchVisibility(visible: Boolean)

    fun toggleLatestLaunchVisibility(visible: Boolean)

    fun togglePinnedList(visible: Boolean)

}