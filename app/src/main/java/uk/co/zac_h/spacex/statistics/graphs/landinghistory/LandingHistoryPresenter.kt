package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
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

    override fun onSuccess(launchDocs: LaunchesExtendedDocsModel?, animate: Boolean) {
        launchDocs?.docs?.let { launches ->
            val stats = ArrayList<LandingHistoryModel>()
            var year = 2012

            launches.forEach { launch ->
                val newYear = launch.launchDateLocal?.substring(0, 4).toString().toIntOrNull()
                    ?: return@forEach
                if (newYear > year) {
                    if (newYear != year++) {
                        for (y in year until newYear) stats.add(LandingHistoryModel(y))
                    }
                    stats.add(LandingHistoryModel(newYear))
                    year = newYear
                }
                launch.cores?.forEach { core ->
                    when (core.landingSuccess) {
                        true -> when (core.landingType) {
                            "Ocean" -> stats[stats.lastIndex].ocean++
                            "RTLS" -> stats[stats.lastIndex].rtls++
                            "ASDS" -> stats[stats.lastIndex].asds++
                        }
                        false -> stats[stats.lastIndex].failures++
                    }
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