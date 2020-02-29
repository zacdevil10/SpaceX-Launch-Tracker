package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CapsulesInteractor {

    fun getCapsules(api: SpaceXInterface, listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(capsules: List<CapsulesModel>?)
        fun onError(error: String)
    }
}