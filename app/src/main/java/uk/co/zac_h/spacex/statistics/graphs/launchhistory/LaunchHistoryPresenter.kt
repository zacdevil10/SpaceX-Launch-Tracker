package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

interface LaunchHistoryPresenter {

    fun getLaunchList(api: SpaceXInterface = SpaceXInterface.create())

    fun addLaunchList(stats: List<HistoryStatsModel>)

    fun showFilter(filterVisible: Boolean)

    fun updateFilter(launches: List<HistoryStatsModel>, filter: String, isFiltered: Boolean)

    fun cancelRequests()

}