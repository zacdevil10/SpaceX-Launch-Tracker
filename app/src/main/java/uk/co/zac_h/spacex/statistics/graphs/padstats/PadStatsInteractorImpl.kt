package uk.co.zac_h.spacex.statistics.graphs.padstats

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class PadStatsInteractorImpl : BaseNetwork(), PadStatsContract.PadStatsInteractor {

    private var launchCall: Call<List<LaunchpadModel>>? = null
    private var landingCall: Call<List<LandingPadModel>>? = null

    override fun getPads(api: SpaceXInterface, listener: PadStatsContract.InteractorCallback) {
        launchCall = api.getLaunchpads().apply {
            makeCall {
                onResponseSuccess = { listener.onGetLaunchpads(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }

        landingCall = api.getLandingPads().apply {
            makeCall {
                onResponseSuccess = { listener.onGetLandingPads(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() {
        landingCall?.cancel()
        launchCall?.cancel()
    }
}