package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.model.spacex.RocketsModel

class LaunchHistoryPresenterImpl(
    private val view: LaunchHistoryView,
    private val interactor: LaunchHistoryInteractor
) : LaunchHistoryPresenter,
    LaunchHistoryInteractor.InteractorCallback {

    private var launchesList = ArrayList<LaunchesModel>()
    private var rocketsList = ArrayList<RocketsModel>()

    override fun getLaunchList() {
        view.showProgress()
        interactor.getLaunches("past", this)
    }

    override fun addLaunchList(launches: ArrayList<LaunchesModel>) {
        onSuccess(launches, false)
    }

    override fun getRocketsList() {
        interactor.getRockets(this)
    }

    override fun addRocketsList(rockets: ArrayList<RocketsModel>) {
        onRocketsSuccess(rockets)
    }

    override fun showFilter(filterVisible: Boolean) {
        view.showFilter(filterVisible)
    }

    override fun updateFilter(
        launches: ArrayList<LaunchesModel>,
        filter: String,
        isFiltered: Boolean
    ) {
        println("Filter: $launchesList")
        when (filter) {
            "success" -> view.setFilterSuccessful(isFiltered)
            "failed" -> view.setFilterFailed(isFiltered)
        }

        if (launchesList.isNotEmpty()) onSuccess(launches, false)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onRocketsSuccess(rockets: List<RocketsModel>?) {
        rockets?.let {
            rocketsList.clear()
            rocketsList.addAll(rockets)

            view.setSuccessRate(rocketsList)
        }
    }

    override fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean) {
        launches?.let {
            launchesList.clear()
            launchesList.addAll(it)

            view.apply {
                hideProgress()
                updatePieChart(launchesList, animate)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}