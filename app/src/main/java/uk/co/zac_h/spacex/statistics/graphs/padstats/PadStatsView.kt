package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.utils.data.LaunchpadModel

interface PadStatsView {

    fun setLaunchesList(launchpads: List<LaunchpadModel>?)

    fun toggleProgress(visibility: Int)

    fun showError(error: String)

}
