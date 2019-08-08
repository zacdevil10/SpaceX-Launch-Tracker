package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface DashboardInteractor {

    fun getSingleLaunch(id: String, listener: DashboardInteractor.InteractorCallback)

    interface InteractorCallback {
        fun onSuccess(id: String, launchesModel: LaunchesModel?)
        fun onError(error: String)
    }

}