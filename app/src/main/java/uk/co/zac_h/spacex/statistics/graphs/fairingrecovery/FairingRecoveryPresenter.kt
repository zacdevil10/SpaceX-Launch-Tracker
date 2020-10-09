package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel

class FairingRecoveryPresenter(
    private val view: FairingRecoveryContract.View,
    private val interactor: FairingRecoveryContract.Interactor
) : FairingRecoveryContract.Presenter, FairingRecoveryContract.Callback {

    override fun getLaunchList(api: SpaceXInterface) {
        view.showProgress()
        interactor.getLaunches(api, this)
    }

    override fun addLaunchList(stats: List<FairingRecoveryModel>) {
        view.updateGraph(stats, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launchDocs: LaunchesExtendedDocsModel?, animate: Boolean) {
        launchDocs?.docs?.let { launches ->
            val stats = ArrayList<FairingRecoveryModel>()
            var year = 2016

            launches.forEach { launch ->
                val newYear = launch.launchDateLocal?.substring(0, 4).toString().toIntOrNull()
                    ?: return@forEach
                if (newYear > year) {
                    if (newYear != year++) {
                        for (y in year until newYear) stats.add(FairingRecoveryModel(y))
                    }
                    stats.add(FairingRecoveryModel(newYear))
                    year = newYear
                }
                when (launch.fairings?.isRecovered) {
                    true -> stats[stats.lastIndex].successes++
                    false -> stats[stats.lastIndex].failures++
                }
            }

            view.apply {
                hideProgress()
                updateGraph(stats, animate)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}