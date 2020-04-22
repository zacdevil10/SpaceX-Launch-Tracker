package uk.co.zac_h.spacex.vehicles.cores

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CoreInteractorImpl : BaseNetwork(), CoreContract.CoreInteractor {

    private var call: Call<List<CoreModel>>? = null

    override fun getCores(api: SpaceXInterface, listener: CoreContract.InteractorCallback) {
        call = api.getCores().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = call?.cancel()
}