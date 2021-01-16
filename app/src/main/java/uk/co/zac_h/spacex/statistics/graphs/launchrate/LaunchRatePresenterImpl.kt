package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
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

    override fun onSuccess(launches: List<Launch>?, animate: Boolean) {
        val rateStatsList = ArrayList<RateStatsModel>()

        println(launches)

        launches?.forEach { launch ->
            val year = launch.launchDate?.dateUnix?.formatDateMillisYYYY() ?: return@forEach

            if (rateStatsList.none { it.year == year }) rateStatsList.add(RateStatsModel(year))

            val stat = rateStatsList.filter { it.year == year }[0]

            if (launch.upcoming == false) {
                launch.success?.let { success ->
                    if (success) {
                        when (launch.rocket?.id) {
                            RocketIds.FALCON_ONE -> {
                                stat.falconOne++
                            }
                            RocketIds.FALCON_NINE -> {
                                stat.falconNine++
                            }
                            RocketIds.FALCON_HEAVY -> {
                                stat.falconHeavy++
                            }
                            else -> {
                                return@forEach
                            }
                        }
                    } else {
                        stat.failure++
                    }
                }
            } else {
                stat.planned++
            }
        }

        println(rateStatsList)

        view.apply {
            hideProgress()
            updateBarChart(rateStatsList, animate)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}