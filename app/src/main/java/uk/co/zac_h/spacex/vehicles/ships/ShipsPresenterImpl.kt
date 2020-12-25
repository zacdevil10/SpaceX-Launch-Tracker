package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class ShipsPresenterImpl(
    private val view: VehiclesContract.View<Ship>,
    private val interactor: VehiclesContract.Interactor<Ship>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<Ship> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() = interactor.cancelAllRequests()

    override fun onSuccess(vehicles: List<Ship>?) {
        vehicles?.let {
            view.updateVehicles(vehicles)
        }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}