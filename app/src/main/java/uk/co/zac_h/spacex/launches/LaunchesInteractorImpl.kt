package uk.co.zac_h.spacex.launches

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchesInteractorImpl : BaseNetwork(), LaunchesInteractor {

    private lateinit var call: Call<List<LaunchesModel>>

    override fun getLaunches(
        id: String,
        order: String,
        api: SpaceXInterface,
        listener: LaunchesInteractor.InteractorCallback
    ) {
        call = api.getLaunches(id, order).apply {
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