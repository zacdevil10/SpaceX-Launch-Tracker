package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.Launch


interface DashboardWearInteractor {

    fun getSingleLaunch(id: String, listener: Callback)

    fun cancelAllRequests(): Unit?

    interface Callback {
        fun onNextSuccess(launch: Launch?)
        fun onLatestSuccess(launch: Launch?)
        fun onError(error: String)
    }
}