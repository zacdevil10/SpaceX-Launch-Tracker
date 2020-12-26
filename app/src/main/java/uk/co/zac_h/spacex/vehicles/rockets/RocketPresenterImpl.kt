package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.spacex.Rocket
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class RocketPresenterImpl(
    private val view: VehiclesContract.View<Rocket>,
    private val interactor: VehiclesContract.Interactor<Rocket>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<Rocket> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(vehicles: List<Rocket>?) {
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