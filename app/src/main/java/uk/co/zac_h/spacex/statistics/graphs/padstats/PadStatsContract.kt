package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.LandingPad
import uk.co.zac_h.spacex.model.spacex.Launchpad
import uk.co.zac_h.spacex.model.spacex.StatsPadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface PadStatsContract {

    interface PadStatsPresenter : NetworkInterface.Presenter<Nothing> {
        fun getLaunchpads(api: SpaceXInterface = SpaceXInterface.create())
        fun getLandingPads(api: SpaceXInterface = SpaceXInterface.create())
    }

    interface PadStatsInteractor : NetworkInterface.Interactor<Nothing> {
        fun getLaunchpads(api: SpaceXInterface, listener: InteractorCallback)
        fun getLandingPads(api: SpaceXInterface, listener: InteractorCallback)
    }

    interface InteractorCallback : NetworkInterface.Callback<Nothing> {
        fun onGetLaunchpads(launchpads: List<Launchpad>?)
        fun onGetLandingPads(landingPads: List<LandingPad>?)
    }
}