package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.DashboardListModel

interface DashboardView {

    fun updateLaunchesList(launches: ArrayList<ArrayList<DashboardListModel>>)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)
}