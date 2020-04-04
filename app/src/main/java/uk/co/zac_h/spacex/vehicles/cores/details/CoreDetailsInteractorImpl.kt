package uk.co.zac_h.spacex.vehicles.cores.details

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CoreDetailsInteractorImpl : BaseNetwork(), CoreDetailsInteractor {

    private lateinit var call: Call<CoreModel>

    override fun getCoreDetails(
        serial: String,
        api: SpaceXInterface,
        listener: CoreDetailsInteractor.InteractorCallback
    ) {
        call = api.getSingleCore(serial).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() {
        if (::call.isInitialized) call.cancel()
    }
}