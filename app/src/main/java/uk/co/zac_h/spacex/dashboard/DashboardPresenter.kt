package uk.co.zac_h.spacex.dashboard

interface DashboardPresenter {

    fun getLatestLaunches(isRefresh: Boolean = false)

    fun cancelRequests()

}