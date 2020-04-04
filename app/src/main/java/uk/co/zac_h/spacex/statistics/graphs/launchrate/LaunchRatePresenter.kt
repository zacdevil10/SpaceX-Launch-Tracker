package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.utils.models.RateStatsModel

interface LaunchRatePresenter {

    fun getLaunchList()

    fun addLaunchList(launches: List<RateStatsModel>)

    fun cancelRequests()

}