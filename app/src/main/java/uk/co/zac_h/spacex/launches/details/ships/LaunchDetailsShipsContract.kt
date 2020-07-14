package uk.co.zac_h.spacex.launches.details.ships

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.model.spacex.ShipExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsShipsContract {

    interface View {
        fun updateShipsRecyclerView(ships: List<ShipExtendedModel>)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getShips(id: String, api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface Interactor {
        fun getShips(id: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest()
    }

    interface InteractorCallback {
        fun onSuccess(launchesExtendedDocsModel: LaunchesExtendedDocsModel?)
        fun onError(error: String)
    }

}