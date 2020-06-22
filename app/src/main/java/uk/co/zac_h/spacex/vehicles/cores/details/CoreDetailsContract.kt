package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CoreDetailsContract {

    interface CoreDetailsView {
        fun updateCoreDetails(coreModel: CoreExtendedModel)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface CoreDetailsPresenter {
        fun getCoreDetails(serial: String, api: SpaceXInterface = SpaceXInterface.create())
        fun addCoreModel(coreModel: CoreExtendedModel)
        fun cancelRequest()
    }

    interface CoreDetailsInteractor {
        fun getCoreDetails(serial: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(coreModel: CoreExtendedModel?)
        fun onError(error: String)
    }
}