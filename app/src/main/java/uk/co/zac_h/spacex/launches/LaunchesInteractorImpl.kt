package uk.co.zac_h.spacex.launches

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchesInteractorImpl : BaseNetwork(), LaunchesContract.LaunchesInteractor {

    private var call: Call<List<LaunchesModel>>? = null

    override fun getLaunches(
        id: String,
        order: String,
        api: SpaceXInterface,
        listener: LaunchesContract.InteractorCallback
    ) {
        call = api.getLaunches(id, order).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = call?.cancel()
}