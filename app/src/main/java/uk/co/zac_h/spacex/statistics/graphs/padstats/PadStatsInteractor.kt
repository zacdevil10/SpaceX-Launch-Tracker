package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel

interface PadStatsInteractor {

    fun getLaunchpads(listener: InteractorCallback)

    fun getLandingPads(listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onGetLaunchpads(launchpads: List<LaunchpadModel>?)
        fun onGetLandingPads(landingPads: List<LandingPadModel>?)
        fun onError(error: String)
    }

}