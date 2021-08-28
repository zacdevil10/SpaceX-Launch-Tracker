package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.LandingPad
import uk.co.zac_h.spacex.dto.spacex.Launchpad
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.SpaceXService

interface PadStatsContract {

    interface PadStatsPresenter : NetworkInterface.Presenter<Nothing> {
        fun getLaunchpads(api: SpaceXService = NetworkModule.providesSpaceXHttpClientV4())
        fun getLandingPads(api: SpaceXService = NetworkModule.providesSpaceXHttpClientV4())
    }

    interface PadStatsInteractor : NetworkInterface.Interactor<Nothing> {
        fun getLaunchpads(api: SpaceXService, listener: InteractorCallback)
        fun getLandingPads(api: SpaceXService, listener: InteractorCallback)
    }

    interface InteractorCallback : NetworkInterface.Callback<Nothing> {
        fun onGetLaunchpads(launchpads: List<Launchpad>?)
        fun onGetLandingPads(landingPads: List<LandingPad>?)
    }
}