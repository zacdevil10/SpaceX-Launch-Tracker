package uk.co.zac_h.spacex.statistics.graphs

import uk.co.zac_h.spacex.utils.data.LaunchesModel

class GraphsPresenterImpl(private val view: GraphsView, private val interactor: GraphsInteractor) : GraphsPresenter, GraphsInteractor.InteractorCallback {

    override fun getLaunchList(id: String) {
        interactor.getLaunches(id, this)
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.setLaunchesList(launches)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}