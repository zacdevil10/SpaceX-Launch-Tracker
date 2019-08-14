package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import android.view.View
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.data.RocketsModel

class LaunchHistoryPresenterImpl(private val view: LaunchHistoryView, private val interactor: LaunchHistoryInteractor) : LaunchHistoryPresenter,
    LaunchHistoryInteractor.InteractorCallback {

    private val rockets = ArrayList<RocketsModel>()

    override fun getLaunchList(id: String) {
        view.toggleProgress(View.VISIBLE)
        interactor.getLaunches(id, this)
    }

    override fun getRocketsList() {
        if (rockets.isNullOrEmpty()) {
            interactor.getRockets(this)
        } else {
            this.onRocketsSuccess(rockets)
        }
    }

    override fun updateFilter(id: String, state: Boolean) {
        view.updateLaunchesList(id, state)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onRocketsSuccess(rockets: List<RocketsModel>?) {
        if (rockets != null) {
            this.rockets.addAll(rockets)

            rockets.forEach {
                view.setSuccessRate(it.id, it.successRate)
            }
        }
    }

    override fun onSuccess(launches: List<LaunchesModel>?) {
        view.toggleProgress(View.GONE)
        view.setLaunchesList(launches)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}