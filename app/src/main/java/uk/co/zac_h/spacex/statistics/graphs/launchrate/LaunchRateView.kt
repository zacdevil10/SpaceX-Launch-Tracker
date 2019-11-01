package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.LaunchesModel

interface LaunchRateView {

    fun updateBarChart(launches: ArrayList<LaunchesModel>, animate: Boolean)

    fun showFilter(filterVisible: Boolean)

    fun setFalconOneFilter(isFiltered: Boolean)

    fun setFalconNineFilter(isFiltered: Boolean)

    fun setFalconHeavyFilter(isFiltered: Boolean)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}