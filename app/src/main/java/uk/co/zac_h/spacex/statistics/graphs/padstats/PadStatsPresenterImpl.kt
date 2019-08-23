package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.view.View
import uk.co.zac_h.spacex.utils.data.LandingPadModel
import uk.co.zac_h.spacex.utils.data.LaunchpadModel

class PadStatsPresenterImpl(
    private val view: PadStatsView,
    private val interactor: PadStatsInteractor
) : PadStatsPresenter, PadStatsInteractor.InteractorCallback {

    override fun getLaunchpads() {
        view.toggleLaunchpadsProgress(View.VISIBLE)
        interactor.getLaunchpads(this)
    }

    override fun getLandingPads() {
        view.toggleLandingPadsProgress(View.VISIBLE)
        interactor.getLandingPads(this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onGetLaunchpads(launchpads: List<LaunchpadModel>?) {
        view.toggleLaunchpadsProgress(View.INVISIBLE)
        view.setLaunchpadsList(launchpads)
    }

    override fun onGetLandingPads(landingPads: List<LandingPadModel>?) {
        view.toggleLandingPadsProgress(View.INVISIBLE)
        view.setLandingPadsList(landingPads)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}