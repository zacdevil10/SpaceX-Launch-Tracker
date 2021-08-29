package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.LandingPad
import uk.co.zac_h.spacex.dto.spacex.Launchpad
import uk.co.zac_h.spacex.dto.spacex.StatsPadModel
import uk.co.zac_h.spacex.retrofit.SpaceXService

class PadStatsPresenterImpl(
    private val view: NetworkInterface.View<List<StatsPadModel>>,
    private val interactor: PadStatsContract.PadStatsInteractor
) : PadStatsContract.PadStatsPresenter, PadStatsContract.InteractorCallback {

    private var padList = ArrayList<StatsPadModel>()

    override fun getLaunchpads(api: SpaceXService) {
        padList.clear()
        view.showProgress()
        interactor.getLaunchpads(api, this)
    }

    override fun getLandingPads(api: SpaceXService) {
        padList.clear()
        view.showProgress()
        interactor.getLandingPads(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onGetLaunchpads(launchpads: List<Launchpad>?) {
        launchpads?.forEach {
            padList.add(
                StatsPadModel(
                    it.fullName,
                    it.launchAttempts ?: 0,
                    it.launchSuccesses ?: 0,
                    it.status
                )
            )
        }

        view.apply {
            hideProgress()
            update(padList)
        }
    }

    override fun onGetLandingPads(landingPads: List<LandingPad>?) {
        landingPads?.forEach {
            padList.add(
                StatsPadModel(
                    it.fullName,
                    it.landingAttempts ?: 0,
                    it.landingSuccesses ?: 0,
                    it.status,
                    it.type
                )
            )
        }

        view.apply {
            hideProgress()
            update(padList)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}