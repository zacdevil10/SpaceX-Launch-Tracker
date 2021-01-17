package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.RocketType
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

class LaunchHistoryPresenterImpl(
    private val view: LaunchHistoryContract.LaunchHistoryView,
    private val interactor: NetworkInterface.Interactor<List<Launch>?>
) : LaunchHistoryContract.LaunchHistoryPresenter,
    NetworkInterface.Callback<List<Launch>?> {

    private lateinit var launchesList: List<HistoryStatsModel>

    override fun getOrUpdate(response: List<HistoryStatsModel>?, api: SpaceXInterface) {
        if (response.isNullOrEmpty()) {
            view.showProgress()
            interactor.get(api, this)
        } else {
            view.update(false, response)
            view.setSuccessRate(response, false)
        }
    }

    override fun showFilter(filterVisible: Boolean) {
        view.toggleFilterVisibility(filterVisible)
    }

    override fun updateFilter(launches: List<HistoryStatsModel>) {
        view.update(false, launches)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: Any, response: List<Launch>?) {
        val falconOne = HistoryStatsModel(RocketType.FALCON_ONE)
        val falconNine = HistoryStatsModel(RocketType.FALCON_NINE)
        val falconHeavy = HistoryStatsModel(RocketType.FALCON_HEAVY)

        response?.forEach {
            when (it.rocket?.id) {
                RocketIds.FALCON_ONE -> {
                    it.success?.let { success ->
                        if (success) falconOne.successes++ else falconOne.failures++
                    }
                    it.rocket?.successRate?.let { successRate ->
                        falconOne.successRate = successRate
                    }
                }
                RocketIds.FALCON_NINE -> {
                    it.success?.let { success ->
                        if (success) falconNine.successes++ else falconNine.failures++
                    }
                    it.rocket?.successRate?.let { successRate ->
                        falconNine.successRate = successRate
                    }
                }
                RocketIds.FALCON_HEAVY -> {
                    it.success?.let { success ->
                        if (success) falconHeavy.successes++ else falconHeavy.failures++
                    }
                    it.rocket?.successRate?.let { successRate ->
                        falconHeavy.successRate = successRate
                    }
                }
            }
        }

        launchesList = listOf(falconOne, falconNine, falconHeavy)

        view.apply {
            hideProgress()
            update(data, launchesList)
            setSuccessRate(launchesList, data as Boolean)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}