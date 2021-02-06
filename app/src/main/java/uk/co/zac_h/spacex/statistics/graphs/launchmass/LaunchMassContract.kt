package uk.co.zac_h.spacex.statistics.graphs.launchmass

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel

interface LaunchMassContract {

    interface View : NetworkInterface.View<List<LaunchMassStatsModel>> {
        fun updateKey(keys: ArrayList<KeysModel>)
        fun showFilter(filterVisible: Boolean)
    }

    interface Presenter : NetworkInterface.Presenter<List<LaunchMassStatsModel>?> {
        fun showFilter(filterVisible: Boolean)
        fun updateFilter(statsList: ArrayList<LaunchMassStatsModel>)
        fun populateOrbitKey(
            f1: OrbitMassModel? = null,
            f9: OrbitMassModel? = null,
            fh: OrbitMassModel? = null
        )

        fun populateRocketKey(
            f1: OrbitMassModel? = null,
            f9: OrbitMassModel? = null,
            fh: OrbitMassModel? = null
        )
    }
}