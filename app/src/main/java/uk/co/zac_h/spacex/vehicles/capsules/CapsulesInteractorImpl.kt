package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CapsulesInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    CapsulesInteractor {

    private var call = api.getCapsules()

    override fun getCapsules(listener: CapsulesInteractor.Callback) =
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }

    override fun cancelAllRequests() = call.cancel()
}