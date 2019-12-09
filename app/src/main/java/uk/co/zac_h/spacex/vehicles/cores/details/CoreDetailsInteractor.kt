package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreModel

interface CoreDetailsInteractor {

    fun getCoreDetails(serial: String, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(coreModel: CoreModel?)
        fun onError(error: String)
    }
}