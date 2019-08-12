package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchRateView {

    fun setLaunchesList(launches: List<LaunchesModel>?)

    fun updateLaunchesList(filterId: String, isFiltered: Boolean)

    fun toggleProgress(visibility: Int)

    fun showError(error: String)

}