package uk.co.zac_h.spacex.launches.details.coredetails

import uk.co.zac_h.spacex.utils.data.CoreModel

interface CoreDetailsInteractor {

    fun getCoreDetails(serial: String, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(coreModel: CoreModel?)
        fun onError(error: String)
    }
}