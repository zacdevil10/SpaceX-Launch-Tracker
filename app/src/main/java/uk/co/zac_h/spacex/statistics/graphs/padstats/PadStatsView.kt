package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.utils.data.LandingPadModel
import uk.co.zac_h.spacex.utils.data.LaunchpadModel

interface PadStatsView {

    fun setLaunchpadsList(launchpads: List<LaunchpadModel>?)

    fun setLandingPadsList(landingPads: List<LandingPadModel>?)

    fun toggleLaunchpadsProgress(visibility: Int)

    fun toggleLandingPadsProgress(visibility: Int)

    fun showError(error: String)

}
