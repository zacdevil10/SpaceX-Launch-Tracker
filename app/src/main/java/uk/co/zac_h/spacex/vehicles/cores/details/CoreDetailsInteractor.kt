package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CoreDetailsInteractor {

    fun getCoreDetails(serial: String, api: SpaceXInterface, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(coreModel: CoreModel?)
        fun onError(error: String)
    }
}