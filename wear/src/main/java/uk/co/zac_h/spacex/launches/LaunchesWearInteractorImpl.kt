package uk.co.zac_h.spacex.launches

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchesWearInteractorImpl : BaseNetwork(), LaunchesWearInteractor {

    private var call: Call<List<LaunchesModel>>? = null

    override fun getAllLaunches(
        id: String,
        order: String,
        listener: LaunchesWearInteractor.Callback
    ) {
        /*call = SpaceXInterface.create().getLaunches(id, order).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelRequest() = call?.cancel()
}