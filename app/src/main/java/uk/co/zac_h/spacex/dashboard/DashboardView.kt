package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface DashboardView {

    fun updateLaunchesList(id: String, launches: LinkedHashMap<String, LaunchesModel>)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)
}