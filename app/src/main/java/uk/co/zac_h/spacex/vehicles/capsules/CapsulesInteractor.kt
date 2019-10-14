package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.CapsulesModel

interface CapsulesInteractor {

    fun getCapsules(listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(capsules: List<CapsulesModel>?)
        fun onError(error: String)
    }
}