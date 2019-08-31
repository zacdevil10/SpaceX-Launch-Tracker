package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.utils.data.LandingPadModel
import uk.co.zac_h.spacex.utils.data.LaunchpadModel
import uk.co.zac_h.spacex.utils.data.StatsPadModel

class PadStatsPresenterImpl(
    private val view: PadStatsView,
    private val interactor: PadStatsInteractor
) : PadStatsPresenter, PadStatsInteractor.InteractorCallback {

    private var padList = ArrayList<StatsPadModel>()

    override fun getPads() {
        if (padList.isEmpty()) {
            view.showProgress()
            interactor.getLaunchpads(this)
            interactor.getLandingPads(this)
        } else {
            view.setPadsList(padList)
        }
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onGetLaunchpads(launchpads: List<LaunchpadModel>?) {
        padList.add(StatsPadModel("Launch Sites", 0, 0, "", isHeading = true))
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
            setPadsList(padList)
            updateRecycler()
        }
    }

    override fun onGetLandingPads(landingPads: List<LandingPadModel>?) {
        padList.add(StatsPadModel("Landing Sites", 0, 0, "", isHeading = true))
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
            setPadsList(padList)
            updateRecycler()
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}