package uk.co.zac_h.spacex.crew

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CrewModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CrewInteractorImpl : BaseNetwork(), CrewContract.CrewInteractor {

    private var call: Call<List<CrewModel>>? = null

    override fun getCrew(api: SpaceXInterface, listener: CrewContract.InteractorCallback) {
        call = api.getCrew().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}