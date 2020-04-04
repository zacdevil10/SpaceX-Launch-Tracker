package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class PadStatsInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    PadStatsInteractor {

    private var launchCall = api.getLaunchpads()
    private var landingCall = api.getLandingPads()

    override fun getPads(listener: PadStatsInteractor.InteractorCallback) {
        launchCall.makeCall {
            onResponseSuccess = { listener.onGetLaunchpads(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }

        landingCall.makeCall {
            onResponseSuccess = { listener.onGetLandingPads(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }
    }

    override fun cancelAllRequests() {
        landingCall.cancel()
        launchCall.cancel()
    }
}