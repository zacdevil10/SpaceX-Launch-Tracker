package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PadType

interface PadStatsInteractor {

    fun getPads(type: PadType, api: SpaceXInterface, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onGetLaunchpads(launchpads: List<LaunchpadModel>?)
        fun onGetLandingPads(landingPads: List<LandingPadModel>?)
        fun onError(error: String)
    }

}