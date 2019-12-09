package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.spacex.CoreModel

interface CoreInteractor {

    fun getCores(listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(cores: List<CoreModel>?)
        fun onError(error: String)
    }
}