package uk.co.zac_h.spacex.launches.details

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsInteractorImpl : BaseNetwork(), LaunchDetailsContract.LaunchDetailsInteractor {

    private var call: Call<LaunchesModel>? = null

    override fun getSingleLaunch(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsContract.InteractorCallback
    ) {
        call = api.getSingleLaunch(id).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelRequest() = terminateAll()
}