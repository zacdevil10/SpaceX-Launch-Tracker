package uk.co.zac_h.spacex.launches.details.cores

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.LaunchCore
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsCoresContract {

    interface View {
        fun updateCoresRecyclerView(coresList: List<LaunchCore>?)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getLaunch(id: String, api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface Interactor {
        fun getCores(id: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest()
    }

    interface InteractorCallback {
        fun onSuccess(launch: Launch?)
        fun onError(error: String)
    }

}