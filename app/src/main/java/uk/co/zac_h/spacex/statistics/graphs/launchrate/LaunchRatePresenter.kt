package uk.co.zac_h.spacex.statistics.graphs.launchrate

interface LaunchRatePresenter {

    fun getLaunchList()

    fun showFilter(filterVisible: Boolean)

    fun updateFilter(id: String, isChecked: Boolean)

    fun cancelRequests()

}