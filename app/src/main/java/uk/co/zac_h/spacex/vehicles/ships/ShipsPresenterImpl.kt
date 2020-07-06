package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.model.spacex.ShipModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class ShipsPresenterImpl(
    private val view: VehiclesContract.View<ShipModel>,
    private val interactor: VehiclesContract.Interactor<ShipModel>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<ShipModel> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(vehicles: List<ShipModel>?) {
        vehicles?.let {
            view.updateVehicles(vehicles)
        }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}