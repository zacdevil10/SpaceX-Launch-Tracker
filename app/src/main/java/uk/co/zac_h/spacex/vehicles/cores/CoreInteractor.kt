package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CoreInteractor {

    fun getCores(api: SpaceXInterface, listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(cores: List<CoreModel>?)
        fun onError(error: String)
    }
}