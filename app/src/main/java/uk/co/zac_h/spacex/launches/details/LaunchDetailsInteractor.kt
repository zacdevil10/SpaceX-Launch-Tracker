package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsInteractor {

    fun getSingleLaunch(id: String, api: SpaceXInterface, listener: InteractorCallback)

    fun cancelRequest()

    interface InteractorCallback {
        fun onSuccess(launchModel: LaunchesModel?)
        fun onError(error: String)
    }
}