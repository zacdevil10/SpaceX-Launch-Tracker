package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import com.github.mikephil.charting.data.PieEntry
import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.model.RocketsModel

class LaunchHistoryPresenterImpl(
    private val view: LaunchHistoryView,
    private val interactor: LaunchHistoryInteractor
) : LaunchHistoryPresenter,
    LaunchHistoryInteractor.InteractorCallback {

    private lateinit var launchesList: List<LaunchesModel>
    private lateinit var rocketsList: List<RocketsModel>

    private var filterSuccessful = false
    private var filterFailed = false

    override fun getLaunchList(id: String) {
        if (!::launchesList.isInitialized) {
            view.showProgress()
            interactor.getLaunches(id, this)
        } else {
            onSuccess(launchesList, false)
        }
    }

    override fun getRocketsList() {
        if (!::rocketsList.isInitialized) {
            interactor.getRockets(this)
        } else {
            onRocketsSuccess(rocketsList)
        }
    }

    override fun showFilter(filterVisible: Boolean) {
        view.showFilter(filterVisible)
    }

    override fun updateFilter(filter: String, isFiltered: Boolean) {
        when (filter) {
            "success" -> filterSuccessful = isFiltered
            "failed" -> filterFailed = isFiltered
        }

        if (::launchesList.isInitialized && launchesList.isNotEmpty()) onSuccess(
            launchesList,
            false
        )
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onRocketsSuccess(rockets: List<RocketsModel>?) {
        rockets?.let {
            if (!::rocketsList.isInitialized) rocketsList = rockets

            rockets.forEach {
                view.setSuccessRate(it.id, it.successRate)
            }
        }
    }

    override fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean) {
        launches?.let {
            if (!::launchesList.isInitialized) launchesList = launches

            val entries = ArrayList<PieEntry>()

            var falconOne = 0f
            var falconNine = 0f
            var falconHeavy = 0f

            launches.forEach {
                if (filterSuccessful && it.success != null && !it.success!!) return@forEach
                if (filterFailed && it.success!!) return@forEach

                when (it.rocket.id) {
                    "falcon1" -> falconOne++
                    "falcon9" -> falconNine++
                    "falconheavy" -> falconHeavy++
                }
            }

            entries.add(PieEntry(falconOne, "Falcon 1"))
            entries.add(PieEntry(falconNine, "Falcon 9"))
            entries.add(PieEntry(falconHeavy, "Falcon Heavy"))

            val centerText = "${launches[0].launchYear} - ${launches[launches.size - 1].launchYear}"

            view.hideProgress()
            view.updatePieChart(entries, centerText, animate)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}