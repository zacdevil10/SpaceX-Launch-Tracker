package uk.co.zac_h.spacex.launches.details.payloads

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.Payload
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsPayloadsContract {

    interface View {
        fun updatePayloadsRecyclerView(payloads: List<Payload>)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getLaunch(id: String, api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface Interactor {
        fun getPayloads(id: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest()
    }

    interface InteractorCallback {
        fun onSuccess(launch: Launch?)
        fun onError(error: String)
    }

}