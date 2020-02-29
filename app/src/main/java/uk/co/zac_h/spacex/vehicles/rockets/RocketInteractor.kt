package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface RocketInteractor {

    fun getRockets(api: SpaceXInterface, listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(rockets: List<RocketsModel>?)
        fun onError(error: String)
    }
}