package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.RateStatsModel

class LaunchRatePresenterImpl(
    private val view: LaunchRateView,
    private val interactor: LaunchRateInteractor
) : LaunchRatePresenter, LaunchRateInteractor.InteractorCallback {

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

    override fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean) {
        launches?.let {
            val rateStatsList = ArrayList<RateStatsModel>()

            var year = 2005
            launches.forEach {
                val newYear = it.launchDateUnix.formatDateMillisYYYY()
                if (newYear > year) {
                    if (newYear != year++) {
                        for (y in year until newYear) rateStatsList.add(RateStatsModel(y))
                    }
                    rateStatsList.add(RateStatsModel(newYear))
                    year = newYear
                }
                if (!it.upcoming) {
                    it.success?.let { success ->
                        if (success) {
                            when (it.rocket.id) {
                                "falcon1" -> {
                                    rateStatsList[rateStatsList.lastIndex].falconOne++
                                }
                                "falcon9" -> {
                                    rateStatsList[rateStatsList.lastIndex].falconNine++
                                }
                                "falconheavy" -> {
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