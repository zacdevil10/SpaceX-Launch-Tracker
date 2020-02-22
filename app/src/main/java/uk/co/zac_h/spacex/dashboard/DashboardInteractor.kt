package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface DashboardInteractor {

    fun getSingleLaunch(id: String, api: SpaceXInterface, listener: InteractorCallback)

    fun hasActiveRequest(): Boolean

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(id: String, launchesModel: LaunchesModel?)
        fun onError(error: String)
    }

}