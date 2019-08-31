package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.utils.data.StatsPadModel

interface PadStatsView {

    fun setPadsList(pads: ArrayList<StatsPadModel>)

    fun updateRecycler()

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}
