package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface DashboardInteractor {

    fun getSingleLaunch(id: String, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(id: String, launchesModel: LaunchesModel?)
        fun onError(error: String)
    }

}