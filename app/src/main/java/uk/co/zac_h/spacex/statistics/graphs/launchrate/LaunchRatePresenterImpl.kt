package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.models.RateStatsModel

class LaunchRatePresenterImpl(
    private val view: LaunchRateContract.LaunchRateView,
    private val interactor: LaunchRateContract.LaunchRateInteractor
) : LaunchRateContract.LaunchRatePresenter, LaunchRateContract.InteractorCallback {

    override fun getLaunchList(api: SpaceXInterface) {
        view.showProgress()
        interactor.getLaunches(api, this)
    }

    override fun addLaunchList(launches: List<RateStatsModel>) {
        view.updateBarChart(launches, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launchDocs: LaunchesExtendedDocsModel?, animate: Boolean) {
        launchDocs?.docs?.let { launches ->
            val rateStatsList = ArrayList<RateStatsModel>()

            var year = 2005
            launches.forEach {
                println(it.launchDateLocal?.substring(0, 4).toString().toIntOrNull())
                val newYear =
                    it.launchDateLocal?.substring(0, 4).toString().toIntOrNull() ?: return@forEach
                if (newYear > year) {
                    if (newYear != year++) {
                        for (y in year until newYear) rateStatsList.add(RateStatsModel(y))
                    }
                    rateStatsList.add(RateStatsModel(newYear))
                    year = newYear
                }
                if (!it.upcoming!!) {
                    it.success?.let { success ->
                        if (success) {
                            when (it.rocket?.id) {
                                RocketIds.FALCON_ONE -> {
                                    rateStatsList[rateStatsList.lastIndex].falconOne++
                                }
                                RocketIds.FALCON_NINE -> {
                                    rateStatsList[rateStatsList.lastIndex].falconNine++
                                }
                                RocketIds.FALCON_HEAVY -> {
                                    rateStatsList[rateStatsList.lastIndex].falconHeavy++
                                }
                                else -> {
                                    return@forEach
                                }
                            }
                        } else {
                            rateStatsList[rateStatsList.lastIndex].failure++
                        }
                    }
                } else {
                    rateStatsList[rateStatsList.lastIndex].planned++
                }
            }

            view.apply {
                hideProgress()
                updateBarChart(rateStatsList, animate)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}