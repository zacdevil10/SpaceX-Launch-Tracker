package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.spacex.LaunchDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.RateStatsModel

interface LaunchRateContract {

    interface LaunchRateView {
        fun updateBarChart(stats: List<RateStatsModel>, animate: Boolean)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface LaunchRatePresenter {
        fun getLaunchList(api: SpaceXInterface = SpaceXInterface.create())
        fun addLaunchList(launches: List<RateStatsModel>)
        fun cancelRequests()
    }

    interface LaunchRateInteractor {
        fun getLaunches(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(launchDocs: LaunchDocsModel?, animate: Boolean)
        fun onError(error: String)
    }

}