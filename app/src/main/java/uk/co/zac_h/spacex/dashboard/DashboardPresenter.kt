package uk.co.zac_h.spacex.dashboard

interface DashboardPresenter {

    fun getLatestLaunches()

    fun getPinnedLaunch(id: String)

    fun cancelRequests()

}