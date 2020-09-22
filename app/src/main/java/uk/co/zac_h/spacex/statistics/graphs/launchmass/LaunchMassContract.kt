package uk.co.zac_h.spacex.statistics.graphs.launchmass

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel

interface LaunchMassContract {

    interface View {
        fun updateData(mass: ArrayList<LaunchMassStatsModel>, animate: Boolean)
        fun updateKey(keys: ArrayList<KeysModel>)
        fun showFilter(filterVisible: Boolean)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getLaunchList(api: SpaceXInterface = SpaceXInterface.create())
        fun addLaunchList(statsList: ArrayList<LaunchMassStatsModel>)
        fun showFilter(filterVisible: Boolean)
        fun updateFilter(statsList: ArrayList<LaunchMassStatsModel>)
        fun populateKey(
            f1: OrbitMassModel? = null,
            f9: OrbitMassModel? = null,
            fh: OrbitMassModel? = null
        )

        fun cancelRequest()
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