package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.spacex.LaunchDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.RocketIds
import uk.co.zac_h.spacex.utils.RocketType
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

class LaunchHistoryPresenterImpl(
    private val view: LaunchHistoryContract.LaunchHistoryView,
    private val interactor: LaunchHistoryContract.LaunchHistoryInteractor
) : LaunchHistoryContract.LaunchHistoryPresenter,
    LaunchHistoryContract.InteractorCallback {

    private lateinit var launchesList: List<HistoryStatsModel>

    override fun getLaunchList(api: SpaceXInterface) {
        view.showProgress()
        interactor.getLaunches(api, this)
    }

    override fun addLaunchList(stats: List<HistoryStatsModel>) {
        view.updatePieChart(stats, false)
        view.setSuccessRate(stats, false)
    }

    override fun showFilter(filterVisible: Boolean) {
        view.toggleFilterVisibility(filterVisible)
    }

    override fun updateFilter(launches: List<HistoryStatsModel>) {
        view.updatePieChart(launches, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launchDocs: LaunchDocsModel?, animate: Boolean) {
        val falconOne = HistoryStatsModel(RocketType.FALCON_ONE)
        val falconNine = HistoryStatsModel(RocketType.FALCON_NINE)
        val falconHeavy = HistoryStatsModel(RocketType.FALCON_HEAVY)

        launchDocs?.docs?.let { launches ->
            launches.forEach {
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
                updatePieChart(launchesList, animate)
                setSuccessRate(launchesList, animate)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}