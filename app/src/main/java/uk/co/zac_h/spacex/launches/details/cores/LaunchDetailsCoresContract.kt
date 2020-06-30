package uk.co.zac_h.spacex.launches.details.cores

import uk.co.zac_h.spacex.model.spacex.LaunchCoreExtendedModel
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsCoresContract {

    interface View {
        fun updateCoresRecyclerView(coresList: List<LaunchCoreExtendedModel>?)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getLaunch(id: String, api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface Interactor {
        fun getSingleLaunch(id: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(launchModel: LaunchesExtendedDocsModel?)
        fun onError(error: String)
    }

}