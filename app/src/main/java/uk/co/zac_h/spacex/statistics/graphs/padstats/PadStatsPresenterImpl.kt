package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.utils.data.LaunchpadModel

class PadStatsPresenterImpl(private val view: PadStatsView, private val interactor: PadStatsInteractor) : PadStatsPresenter, PadStatsInteractor.InteractorCallback {

    override fun getLaunchpads() {
        interactor.getLaunchpads(this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(launchpads: List<LaunchpadModel>?) {
        view.setLaunchesList(launchpads)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}