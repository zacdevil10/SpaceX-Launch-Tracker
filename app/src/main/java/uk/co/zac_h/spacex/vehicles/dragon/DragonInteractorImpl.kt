package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DragonInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    DragonInteractor {

    private val call = api.getDragons()

    override fun getDragon(listener: DragonInteractor.Callback) =
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }

    override fun cancelRequest() = call.cancel()

}