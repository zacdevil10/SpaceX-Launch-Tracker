package uk.co.zac_h.spacex.launches.details.payloads

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.model.spacex.PayloadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsPayloadsContract {

    interface View {
        fun updatePayloadsRecyclerView(payloadsList: List<PayloadModel>?)
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
        fun cancelRequest()
    }

    interface InteractorCallback {
        fun onSuccess(launchModel: LaunchesExtendedDocsModel?)
        fun onError(error: String)
    }

}