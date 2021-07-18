package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.SPACEX_BASE_URL_V5
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import uk.co.zac_h.spacex.utils.models.RateStatsModel

class LaunchRatePresenterImpl(
    private val view: NetworkInterface.View<List<RateStatsModel>>,
    private val interactor: NetworkInterface.Interactor<List<Launch>?>
) : NetworkInterface.Presenter<List<RateStatsModel>?>, NetworkInterface.Callback<List<Launch>?> {

    override fun getOrUpdate(response: List<RateStatsModel>?, api: SpaceXInterface) {
        if (response.isNullOrEmpty()) {
            view.showProgress()
            interactor.get(SpaceXInterface.create(SPACEX_BASE_URL_V5), this)
        } else view.update(false, response)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: Any, response: List<Launch>?) {
        val rateStatsList = ArrayList<RateStatsModel>()

        response?.forEach { launch ->
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
            update(data, rateStatsList)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}