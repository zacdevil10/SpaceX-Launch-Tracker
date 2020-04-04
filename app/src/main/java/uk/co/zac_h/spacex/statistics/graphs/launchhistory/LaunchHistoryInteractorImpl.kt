package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchHistoryInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    LaunchHistoryInteractor {

    private var call = api.getLaunches("past", "asc")

    override fun getLaunches(listener: LaunchHistoryInteractor.InteractorCallback) =
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body(), true) }
            onResponseFailure = { listener.onError(it) }
        }

    override fun cancelAllRequests() = call.cancel()

}