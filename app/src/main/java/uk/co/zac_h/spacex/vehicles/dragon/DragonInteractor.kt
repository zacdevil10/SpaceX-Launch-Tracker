package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface DragonInteractor {

    fun getDragon(api: SpaceXInterface, listener: Callback)

    fun cancelRequest()

    interface Callback {
        fun onSuccess(dragon: List<DragonModel>?)
        fun onError(error: String)
    }
}