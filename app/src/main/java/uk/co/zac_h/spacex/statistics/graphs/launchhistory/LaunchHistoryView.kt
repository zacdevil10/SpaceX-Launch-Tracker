package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchHistoryView {

    fun setSuccessRate(id: Int, percent: Int)

    fun setLaunchesList(launches: List<LaunchesModel>?)

    fun updateLaunchesList(filter: String, isFiltered: Boolean)

    fun toggleProgress(visibility: Int)

    fun showError(error: String)

}