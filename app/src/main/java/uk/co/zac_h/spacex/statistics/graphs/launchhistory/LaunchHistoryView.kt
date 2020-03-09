package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

interface LaunchHistoryView {

    fun setSuccessRate(stats: List<HistoryStatsModel>, animate: Boolean)

    fun updatePieChart(stats: List<HistoryStatsModel>, animate: Boolean)

    fun showFilter(filterVisible: Boolean)

    fun setFilterSuccessful(isFiltered: Boolean)

    fun setFilterFailed(isFiltered: Boolean)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}