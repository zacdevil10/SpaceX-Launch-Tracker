package uk.co.zac_h.spacex.vehicles.rockets

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class RocketInteractorImpl : BaseNetwork(),
    RocketContract.RocketInteractor {

    private var call: Call<List<RocketsModel>>? = null

    override fun getRockets(api: SpaceXInterface, listener: RocketContract.InteractorCallback) {
        call = api.getRockets().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}