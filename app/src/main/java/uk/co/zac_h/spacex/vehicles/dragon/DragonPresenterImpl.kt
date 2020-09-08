package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class DragonPresenterImpl(
    private val view: VehiclesContract.View<DragonModel>,
    private val interactor: VehiclesContract.Interactor<DragonModel>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<DragonModel> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(vehicles: List<DragonModel>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            vehicles?.let { updateVehicles(it.reversed()) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }

}