package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.spacex.RocketsModel

interface RocketInteractor {

    fun getRockets(listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(rockets: List<RocketsModel>?)
        fun onError(error: String)
    }
}