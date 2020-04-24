package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchHistoryInteractorImpl : BaseNetwork(), LaunchHistoryContract.LaunchHistoryInteractor {

    private var call: Call<List<LaunchesModel>>? = null

    override fun getLaunches(
        api: SpaceXInterface,
        listener: LaunchHistoryContract.InteractorCallback
    ) {
        call = api.getLaunches("past", "asc").apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body(), true) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}