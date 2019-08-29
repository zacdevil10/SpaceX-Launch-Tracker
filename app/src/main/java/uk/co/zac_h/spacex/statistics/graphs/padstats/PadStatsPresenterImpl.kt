package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.utils.data.LandingPadModel
import uk.co.zac_h.spacex.utils.data.LaunchpadModel

class PadStatsPresenterImpl(
    private val view: PadStatsView,
    private val interactor: PadStatsInteractor
) : PadStatsPresenter, PadStatsInteractor.InteractorCallback {

    override fun getPads() {
        view.showProgress()
        interactor.getLaunchpads(this)
        interactor.getLandingPads(this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onGetLaunchpads(launchpads: List<LaunchpadModel>?) {
        view.hideProgress()
        view.setLaunchpadsList(launchpads)
    }

    override fun onGetLandingPads(landingPads: List<LandingPadModel>?) {
        view.setLandingPadsList(landingPads)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}