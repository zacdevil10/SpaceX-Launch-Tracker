package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.model.spacex.ShipExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class ShipsPresenterImpl(
    private val view: VehiclesContract.View<ShipExtendedModel>,
    private val interactor: VehiclesContract.Interactor<ShipExtendedModel>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<ShipExtendedModel> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(vehicles: List<ShipExtendedModel>?) {
        vehicles?.let {
            view.updateVehicles(vehicles)
        }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}