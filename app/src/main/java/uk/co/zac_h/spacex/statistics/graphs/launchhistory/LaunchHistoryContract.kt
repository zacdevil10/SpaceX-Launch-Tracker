package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

interface LaunchHistoryContract {

    interface LaunchHistoryView : NetworkInterface.View<List<HistoryStatsModel>> {
        fun setSuccessRate(stats: List<HistoryStatsModel>, animate: Boolean)
        fun toggleFilterVisibility(filterVisible: Boolean)
    }

    interface LaunchHistoryPresenter : NetworkInterface.Presenter<List<HistoryStatsModel>?> {
        fun showFilter(filterVisible: Boolean)
        fun updateFilter(launches: List<HistoryStatsModel>)
    }

}