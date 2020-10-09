package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.LandingHistoryModel

interface LandingHistoryContract {

    interface View {
        fun updateGraph(stats: List<LandingHistoryModel>, animate: Boolean)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getLaunchList(api: SpaceXInterface = SpaceXInterface.create())
        fun addLaunchList(stats: List<LandingHistoryModel>)
        fun cancelRequests()
    }

    interface Interactor {
        fun getLaunches(api: SpaceXInterface, listener: Callback)
        fun cancelAllRequests()
    }

    interface Callback {
        fun onSuccess(launchDocs: LaunchesExtendedDocsModel?, animate: Boolean)
        fun onError(error: String)
    }

}