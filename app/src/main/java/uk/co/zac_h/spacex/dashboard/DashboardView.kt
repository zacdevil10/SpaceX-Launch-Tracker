package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface DashboardView {

    fun updateLaunchesList(id: String, launchesModel: LaunchesModel?)

    fun toggleProgress(visibility: Int)

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)
}