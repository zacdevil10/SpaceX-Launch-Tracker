package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.spacex.LaunchesModel

interface LaunchRatePresenter {

    fun getLaunchList()

    fun addLaunchList(launches: ArrayList<LaunchesModel>)

    fun showFilter(filterVisible: Boolean)

    fun updateFilter(launches: ArrayList<LaunchesModel>, id: String, isChecked: Boolean)

    fun cancelRequests()

}