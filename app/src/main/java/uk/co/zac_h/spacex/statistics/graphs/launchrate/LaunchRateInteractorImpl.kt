package uk.co.zac_h.spacex.statistics.graphs.launchrate

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchRateInteractorImpl : BaseNetwork(), LaunchRateContract.LaunchRateInteractor {

    private var call: Call<List<LaunchesModel>>? = null

    override fun getLaunches(
        api: SpaceXInterface,
        listener: LaunchRateContract.InteractorCallback
    ) {
        call = api.getLaunches().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body(), true) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}