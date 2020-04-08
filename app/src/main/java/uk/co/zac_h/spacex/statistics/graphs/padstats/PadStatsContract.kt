package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel
import uk.co.zac_h.spacex.model.spacex.StatsPadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface PadStatsContract {

    interface PadStatsView {
        fun updateRecycler(pads: ArrayList<StatsPadModel>)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface PadStatsPresenter {
        fun getPads(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequests()
    }

    interface PadStatsInteractor {
        fun getPads(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests()
    }

    interface InteractorCallback {
        fun onGetLaunchpads(launchpads: List<LaunchpadModel>?)
        fun onGetLandingPads(landingPads: List<LandingPadModel>?)
        fun onError(error: String)
    }
}