package uk.co.zac_h.spacex.launches.details.core

import uk.co.zac_h.spacex.model.CoreModel

interface CoreDetailsInteractor {

    fun getCoreDetails(serial: String, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(coreModel: CoreModel?)
        fun onError(error: String)
    }
}