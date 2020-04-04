package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.model.spacex.DragonModel

interface DragonInteractor {

    fun getDragon(listener: Callback)

    fun cancelRequest()

    interface Callback {
        fun onSuccess(dragon: List<DragonModel>?)
        fun onError(error: String)
    }
}