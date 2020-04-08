package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CoreDetailsContract {

    interface CoreDetailsView {
        fun updateCoreDetails(coreModel: CoreModel)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface CoreDetailsPresenter {
        fun getCoreDetails(serial: String, api: SpaceXInterface = SpaceXInterface.create())
        fun addCoreModel(coreModel: CoreModel)
        fun cancelRequest()
    }

    interface CoreDetailsInteractor {
        fun getCoreDetails(serial: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(coreModel: CoreModel?)
        fun onError(error: String)
    }
}