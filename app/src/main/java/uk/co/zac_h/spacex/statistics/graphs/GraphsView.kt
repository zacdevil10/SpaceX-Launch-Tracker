package uk.co.zac_h.spacex.statistics.graphs

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface GraphsView {

    fun setLaunchesList(launches: List<LaunchesModel>?)

    fun updateLaunchesList(filter: String, isFiltered: Boolean)

    fun showError(error: String)

}