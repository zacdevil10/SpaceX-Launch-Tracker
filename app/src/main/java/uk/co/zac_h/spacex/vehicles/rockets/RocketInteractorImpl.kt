package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class RocketInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    RocketInteractor {

    private val call = api.getRockets()

    override fun getRockets(listener: RocketInteractor.Callback) =
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }

    override fun cancelAllRequests() = call.cancel()
}