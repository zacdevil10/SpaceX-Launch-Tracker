package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchRateInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    LaunchRateInteractor {

    private var call = api.getLaunches()

    override fun getLaunches(listener: LaunchRateInteractor.InteractorCallback) =
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body(), true) }
            onResponseFailure = { listener.onError(it) }
        }

    override fun cancelAllRequests() = call.cancel()
}