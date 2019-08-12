package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.data.RocketsModel

class LaunchHistoryPresenterImpl(private val view: LaunchHistoryView, private val interactor: LaunchHistoryInteractor) : LaunchHistoryPresenter,
    LaunchHistoryInteractor.InteractorCallback {

    override fun getLaunchList(id: String) {
        interactor.getLaunches(id, this)
    }

    override fun getRocketsList() {
        interactor.getRockets(this)
    }

    override fun updateFilter(id: String, state: Boolean) {
        view.updateLaunchesList(id, state)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onRocketsSuccess(rockets: List<RocketsModel>?) {
        rockets?.forEach {
            view.setSuccessRate(it.id, it.successRate)
        }
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.setLaunchesList(launches)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}