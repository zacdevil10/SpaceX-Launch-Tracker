package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.utils.models.RateStatsModel

interface LaunchRateView {

    fun updateBarChart(stats: List<RateStatsModel>, animate: Boolean)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}