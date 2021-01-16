package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.LandingHistoryModel

class LandingHistoryPresenter(
    private val view: LandingHistoryContract.View,
    private val interactor: LandingHistoryContract.Interactor
) : LandingHistoryContract.Presenter, LandingHistoryContract.Callback {

    override fun getLaunchList(api: SpaceXInterface) {
        view.showProgress()
        interactor.getLaunches(api, this)
    }

    override fun addLaunchList(stats: List<LandingHistoryModel>) {
        view.updateGraph(stats, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<Launch>?, animate: Boolean) {
        val stats = ArrayList<LandingHistoryModel>()

        launches?.forEach { launch ->
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
            updateGraph(stats, animate)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}