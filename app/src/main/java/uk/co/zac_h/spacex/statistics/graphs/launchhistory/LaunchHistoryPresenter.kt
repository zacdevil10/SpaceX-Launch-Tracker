package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.model.spacex.RocketsModel

interface LaunchHistoryPresenter {

    fun getLaunchList()

    fun addLaunchList(launches: ArrayList<LaunchesModel>)

    fun getRocketsList()

    fun addRocketsList(rockets: ArrayList<RocketsModel>)

    fun showFilter(filterVisible: Boolean)

    fun updateFilter(launches: ArrayList<LaunchesModel>, filter: String, isFiltered: Boolean)

    fun cancelRequests()

}