package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel

class FairingRecoveryPresenter(
    private val view: NetworkInterface.View<List<FairingRecoveryModel>>,
    private val interactor: NetworkInterface.Interactor<List<Launch>?>
) : NetworkInterface.Presenter<List<FairingRecoveryModel>?>, NetworkInterface.Callback<List<Launch>?> {

    override fun getOrUpdate(response: List<FairingRecoveryModel>?, api: SpaceXService) {
        if (response.isNullOrEmpty()) {
            view.showProgress()
            interactor.get(api, this)
        } else {
            view.apply {
                hideProgress()
                update(false, response)
            }
        }
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: Any, response: List<Launch>?) {
        val stats = ArrayList<FairingRecoveryModel>()

        response?.forEach { launch ->
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
            update(data, stats)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}