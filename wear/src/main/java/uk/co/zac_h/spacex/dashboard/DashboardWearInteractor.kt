package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.LaunchesModel

interface DashboardWearInteractor {

    fun getSingleLaunch(id: String, listener: Callback)

    fun cancelAllRequests(): Unit?

    interface Callback {
        fun onNextSuccess(launch: LaunchesModel?)
        fun onLatestSuccess(launch: LaunchesModel?)
        fun onError(error: String)
    }
}