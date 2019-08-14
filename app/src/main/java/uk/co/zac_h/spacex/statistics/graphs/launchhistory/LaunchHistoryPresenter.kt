package uk.co.zac_h.spacex.statistics.graphs.launchhistory

interface LaunchHistoryPresenter {

    fun getLaunchList(id: String)

    fun getRocketsList()

    fun updateFilter(id: String, state: Boolean)

    fun cancelRequests()

}