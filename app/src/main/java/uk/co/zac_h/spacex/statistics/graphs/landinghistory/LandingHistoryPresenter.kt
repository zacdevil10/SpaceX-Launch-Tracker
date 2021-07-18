package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL_V5
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.LandingHistoryModel

class LandingHistoryPresenter(
    private val view: NetworkInterface.View<List<LandingHistoryModel>>,
    private val interactor: NetworkInterface.Interactor<List<Launch>?>
) : NetworkInterface.Presenter<List<LandingHistoryModel>?>, NetworkInterface.Callback<List<Launch>?> {

    override fun getOrUpdate(response: List<LandingHistoryModel>?, api: SpaceXInterface) {
        if (response.isNullOrEmpty()) {
            view.showProgress()
            interactor.get(SpaceXInterface.create(SPACEX_BASE_URL_V5), this)
        } else view.apply {
            hideProgress()
            update(false, response)
        }
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: Any, response: List<Launch>?) {
        val stats = ArrayList<LandingHistoryModel>()

        response?.forEach { launch ->
            val year = launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

            if (stats.none { it.year == year }) stats.add(LandingHistoryModel(year))

            val stat = stats.filter { it.year == year }[0]

            launch.cores?.forEach { core ->
                when (core.landingSuccess) {
                    true -> when (core.landingType) {
                        "Ocean" -> stat.ocean++
                        "RTLS" -> stat.rtls++
                        "ASDS" -> stat.asds++
                    }
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