package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.model.spacex.RocketsModel

interface LaunchHistoryView {

    fun setSuccessRate(rockets: ArrayList<RocketsModel>)

    fun updatePieChart(launches: ArrayList<LaunchesModel>, animate: Boolean)

    fun showFilter(filterVisible: Boolean)

    fun setFilterSuccessful(isFiltered: Boolean)

    fun setFilterFailed(isFiltered: Boolean)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}