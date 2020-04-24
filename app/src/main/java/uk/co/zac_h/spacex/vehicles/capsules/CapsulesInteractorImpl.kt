package uk.co.zac_h.spacex.vehicles.capsules

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CapsulesInteractorImpl : BaseNetwork(), CapsulesContract.CapsulesInteractor {

    private var call: Call<List<CapsulesModel>>? = null

    override fun getCapsules(api: SpaceXInterface, listener: CapsulesContract.InteractorCallback) {
        call = api.getCapsules().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}