package uk.co.zac_h.spacex.dashboard

interface DashboardPresenter {

    fun getSingleLaunch(id: String)

    fun cancelRequests()

}