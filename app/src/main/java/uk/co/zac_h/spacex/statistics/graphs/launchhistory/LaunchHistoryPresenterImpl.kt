package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel
import kotlin.math.roundToInt

class LaunchHistoryPresenterImpl(
    private val view: LaunchHistoryView,
    private val interactor: LaunchHistoryInteractor
) : LaunchHistoryPresenter,
    LaunchHistoryInteractor.InteractorCallback {

    private lateinit var launchesList: List<HistoryStatsModel>

    override fun getLaunchList(api: SpaceXInterface) {
        view.showProgress()
        interactor.getLaunches(api, this)
    }

    override fun addLaunchList(stats: List<HistoryStatsModel>) {
        view.updatePieChart(stats, false)
        view.setSuccessRate(stats)
    }

    override fun showFilter(filterVisible: Boolean) {
        view.showFilter(filterVisible)
    }

    override fun updateFilter(
        launches: List<HistoryStatsModel>,
        filter: String,
        isFiltered: Boolean
    ) {
        when (filter) {
            "success" -> view.setFilterSuccessful(isFiltered)
            "failed" -> view.setFilterFailed(isFiltered)
        }

        view.updatePieChart(launches, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean) {
        val falconOne = HistoryStatsModel("falcon1")
        val falconNine = HistoryStatsModel("falcon9")
        val falconHeavy = HistoryStatsModel("falconheavy")

        launches?.let {
            launches.forEach {
                when (it.rocket.id) {
                    "falcon1" -> it.success?.let { success ->
                        if (success) falconOne.successes++ else falconOne.failures++
                    }
                    "falcon9" -> it.success?.let { success ->
                        if (success) falconNine.successes++ else falconNine.failures++
                    }
                    "falconheavy" -> it.success?.let { success ->
                        if (success) falconHeavy.successes++ else falconHeavy.failures++
                    }
                }
            }

            falconOne.successRate =
                falconOne.successes.toFloat().div(falconOne.successes + falconOne.failures)
                    .times(100).roundToInt()
            falconNine.successRate =
                falconNine.successes.toFloat().div(falconNine.successes + falconNine.failures)
                    .times(100).roundToInt()
            falconHeavy.successRate =
                falconHeavy.successes.toFloat().div(falconHeavy.successes + falconHeavy.failures)
                    .times(100).roundToInt()

            launchesList = listOf(falconOne, falconNine, falconHeavy)

            view.apply {
                hideProgress()
                updatePieChart(launchesList, animate)
                setSuccessRate(launchesList)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}