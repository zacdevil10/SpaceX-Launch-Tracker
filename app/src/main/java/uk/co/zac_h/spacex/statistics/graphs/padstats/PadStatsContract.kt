package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.model.spacex.LandingPadDocsModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadDocsModel
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
        fun getLaunchpads(api: SpaceXInterface = SpaceXInterface.create())
        fun getLandingPads(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequests()
    }

    interface PadStatsInteractor {
        fun getLaunchpads(api: SpaceXInterface, listener: InteractorCallback)
        fun getLandingPads(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests()
    }

    interface InteractorCallback {
        fun onGetLaunchpads(launchpads: LaunchpadDocsModel?)
        fun onGetLandingPads(landingPads: LandingPadDocsModel?)
        fun onError(error: String)
    }
}