package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel

interface FairingRecoveryContract {

    interface View {
        fun updateGraph(stats: List<FairingRecoveryModel>, animate: Boolean)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getLaunchList(api: SpaceXInterface = SpaceXInterface.create())
        fun addLaunchList(stats: List<FairingRecoveryModel>)
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