package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel

class FairingRecoveryPresenter(
    private val view: NetworkInterface.View<List<FairingRecoveryModel>>,
    private val interactor: NetworkInterface.Interactor<List<Launch>?>
) : NetworkInterface.Presenter<List<FairingRecoveryModel>?>, NetworkInterface.Callback<List<Launch>?> {

    override fun getOrUpdate(response: List<FairingRecoveryModel>?, api: SpaceXInterface) {
        if (response.isNullOrEmpty()) {
            view.showProgress()
            interactor.get(api, this)
        } else view.update(false, response)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: Any, response: List<Launch>?) {
        val stats = ArrayList<FairingRecoveryModel>()

        response?.filter { launch ->
            launch.fairings?.any { it.netAttempt == true || it.waterAttempt == true } == true
        }?.forEach { launch ->
            val year = launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

            if (stats.none { it.year == year }) stats.add(FairingRecoveryModel(year))

            val stat = stats.filter { it.year == year }[0]

            launch.fairings?.forEach {
                when (it.recovered == true) {
                    true -> stat.successes++
                    false -> stat.failures++
                }
            }
        }

        view.apply {
            hideProgress()
            update(data, stats)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}