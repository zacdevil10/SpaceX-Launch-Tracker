package uk.co.zac_h.spacex.vehicles.cores.details

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CoreExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CoreDetailsInteractorImpl : BaseNetwork(), CoreDetailsContract.CoreDetailsInteractor {

    private var call: Call<CoreExtendedModel>? = null

    override fun getCoreDetails(
        serial: String,
        api: SpaceXInterface,
        listener: CoreDetailsContract.InteractorCallback
    ) {
        call = api.getSingleCore(serial).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}