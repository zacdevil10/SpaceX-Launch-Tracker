package uk.co.zac_h.spacex.statistics.graphs

import uk.co.zac_h.spacex.statistics.graphs.ui.TotalLaunchesView
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.data.RocketsModel

class GraphsPresenterImpl(private val view: TotalLaunchesView, private val graphsView: GraphsView, private val interactor: GraphsInteractor) : GraphsPresenter, GraphsInteractor.InteractorCallback {

    override fun getLaunchList(id: String) {
        interactor.getLaunches(id, this)
    }

    override fun getRocketsList() {
        interactor.getRockets(this)
    }

    override fun updateFilter(id: String, state: Boolean) {
        graphsView.updateLaunchesList(id, state)
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
        graphsView.setLaunchesList(launches)
    }

    override fun onError(error: String) {
        graphsView.showError(error)
    }
}