package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

interface LaunchHistoryContract {

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

    interface LaunchHistoryPresenter {
        fun getLaunchList(api: SpaceXInterface = SpaceXInterface.create())
        fun addLaunchList(stats: List<HistoryStatsModel>)
        fun showFilter(filterVisible: Boolean)
        fun updateFilter(launches: List<HistoryStatsModel>, filter: String, isFiltered: Boolean)
        fun cancelRequests()
    }

    interface LaunchHistoryInteractor {
        fun getLaunches(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean)
        fun onError(error: String)
    }
}