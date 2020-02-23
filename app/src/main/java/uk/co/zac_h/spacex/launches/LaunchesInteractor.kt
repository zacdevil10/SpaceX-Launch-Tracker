package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchesInteractor {

    fun getLaunches(id: String, order: String, api: SpaceXInterface, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(launches: List<LaunchesModel>?)
        fun onError(error: String)
    }

}