package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.model.StatsPadModel

interface PadStatsView {

    fun updateRecycler(pads: ArrayList<StatsPadModel>)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}
