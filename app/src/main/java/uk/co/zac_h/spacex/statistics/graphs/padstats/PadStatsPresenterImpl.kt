package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel
import uk.co.zac_h.spacex.model.spacex.StatsPadModel

class PadStatsPresenterImpl(
    private val view: PadStatsView,
    private val interactor: PadStatsInteractor
) : PadStatsPresenter, PadStatsInteractor.InteractorCallback {

    private var padList = ArrayList<StatsPadModel>()

    override fun getPads() {
        padList.clear()
        view.showProgress()
        interactor.apply {
            getLaunchpads(this@PadStatsPresenterImpl)
            getLandingPads(this@PadStatsPresenterImpl)
        }
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onGetLaunchpads(launchpads: List<LaunchpadModel>?) {
        padList.add(
            StatsPadModel(
                "Launch Sites",
                0,
                0,
                "",
                isHeading = true
            )
        )
        launchpads?.forEach {
            padList.add(
                StatsPadModel(
                    it.nameLong,
                    it.launchAttempts,
                    it.launchSuccesses,
                    it.status
                )
            )
        }

        view.apply {
            hideProgress()
            updateRecycler(padList)
        }
    }

    override fun onGetLandingPads(landingPads: List<LandingPadModel>?) {
        padList.add(
            StatsPadModel(
                "Landing Sites",
                0,
                0,
                "",
                isHeading = true
            )
        )
        landingPads?.forEach {
            padList.add(
                StatsPadModel(
                    it.nameFull,
                    it.landingAttempts,
                    it.landingSuccesses,
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