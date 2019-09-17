package uk.co.zac_h.spacex.dashboard

interface DashboardWearPresenter {

    fun getLaunch(id: String)

    fun updateCountdown(time: Long)

    fun cancelRequests()
}