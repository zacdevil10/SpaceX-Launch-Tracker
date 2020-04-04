package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CoreInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    CoreInteractor {

    private val call = api.getCores()

    override fun getCores(listener: CoreInteractor.Callback) =
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }

    override fun cancelAllRequests() = call.cancel()
}