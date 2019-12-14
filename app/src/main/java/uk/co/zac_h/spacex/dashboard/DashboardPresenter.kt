package uk.co.zac_h.spacex.dashboard

interface DashboardPresenter {

    fun getLatestLaunches()

    fun updateCountdown(time: Long)

    fun cancelRequests()

}