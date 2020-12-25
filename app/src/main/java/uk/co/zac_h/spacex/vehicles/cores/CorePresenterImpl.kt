package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.spacex.Core
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class CorePresenterImpl(
    private val view: VehiclesContract.View<Core>,
    private val interactor: VehiclesContract.Interactor<Core>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<Core> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(vehicles: List<Core>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            vehicles?.let { updateVehicles(it) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}