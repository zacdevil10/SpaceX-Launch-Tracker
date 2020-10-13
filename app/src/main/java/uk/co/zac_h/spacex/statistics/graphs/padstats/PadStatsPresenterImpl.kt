package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.model.spacex.LandingPadDocsModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadDocsModel
import uk.co.zac_h.spacex.model.spacex.StatsPadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class PadStatsPresenterImpl(
    private val view: PadStatsContract.PadStatsView,
    private val interactor: PadStatsContract.PadStatsInteractor
) : PadStatsContract.PadStatsPresenter, PadStatsContract.InteractorCallback {

    private var padList = ArrayList<StatsPadModel>()

    override fun getLaunchpads(api: SpaceXInterface) {
        padList.clear()
        view.showProgress()
        interactor.getLaunchpads(api, this)
    }

    override fun getLandingPads(api: SpaceXInterface) {
        padList.clear()
        view.showProgress()
        interactor.getLandingPads(api, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onGetLaunchpads(launchpads: LaunchpadDocsModel?) {
        launchpads?.docs?.forEach {
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
            updateRecycler(padList)
        }
    }

    override fun onGetLandingPads(landingPads: LandingPadDocsModel?) {
        landingPads?.docs?.forEach {
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
            updateRecycler(padList)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}