package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.RateStatsModel

class LaunchRatePresenterImpl(
    private val view: LaunchRateView,
    private val interactor: LaunchRateInteractor
) : LaunchRatePresenter, LaunchRateInteractor.InteractorCallback {

    private var launchesList = ArrayList<RateStatsModel>()

    override fun getLaunchList() {
        view.showProgress()
        interactor.getLaunches(this)
    }

    override fun addLaunchList(launches: ArrayList<RateStatsModel>) {
        view.updateBarChart(launches, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean) {
        launches?.let {
            launchesList.clear()

            var year = 2005
            launches.forEach {
                val newYear = it.launchDateUnix.formatDateMillisYYYY()
                if (newYear > year) {
                    if (newYear != year++) {
                        for (y in year until newYear) launchesList.add(RateStatsModel(y))
                    }
                    launchesList.add(RateStatsModel(newYear))
                    year = newYear
                }
                if (!it.upcoming) {
                    it.success?.let { success ->
                        if (success) {
                            when (it.rocket.id) {
                                "falcon1" -> {
                                    launchesList[launchesList.lastIndex].falconOne++
                                }
                                "falcon9" -> {
                                    launchesList[launchesList.lastIndex].falconNine++
                                }
                                "falconheavy" -> {
                                    launchesList[launchesList.lastIndex].falconHeavy++
                                }
                                else -> {
                                    return@forEach
                                }
                            }
                        } else {
                            launchesList[launchesList.lastIndex].failure++
                        }
                    }
                } else {
                    launchesList[launchesList.lastIndex].planned++
                }
            }

            view.apply {
                hideProgress()
                updateBarChart(launchesList, animate)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}