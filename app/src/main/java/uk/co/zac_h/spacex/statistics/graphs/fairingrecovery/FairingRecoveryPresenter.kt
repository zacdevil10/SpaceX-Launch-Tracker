package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
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

    override fun onSuccess(launches: List<Launch>?, animate: Boolean) {
        val stats = ArrayList<FairingRecoveryModel>()

        launches?.forEach { launch ->
            val year = launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

            if (stats.none { it.year == year }) stats.add(FairingRecoveryModel(year))

            val stat = stats.filter { it.year == year }[0]

            when (launch.fairings?.recoveryAttempt == true && launch.fairings?.isRecovered == true) {
                true -> stat.successes++
                false -> stat.failures++
            }
        }

        view.apply {
            hideProgress()
            updateGraph(stats, animate)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}