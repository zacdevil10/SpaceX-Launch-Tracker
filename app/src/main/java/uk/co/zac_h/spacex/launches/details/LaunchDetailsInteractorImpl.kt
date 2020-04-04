package uk.co.zac_h.spacex.launches.details

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsInteractorImpl : BaseNetwork(), LaunchDetailsInteractor {

    private lateinit var call: Call<LaunchesModel>

    override fun getSingleLaunch(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsInteractor.InteractorCallback
    ) {
        call = api.getSingleLaunch(id).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelRequest() {
        if (::call.isInitialized) call.cancel()
    }
}