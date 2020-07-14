package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class CapsulesPresenterImpl(
    private val view: VehiclesContract.View<CapsulesModel>,
    private val interactor: VehiclesContract.Interactor<CapsulesModel>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<CapsulesModel> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(vehicles: List<CapsulesModel>?) {
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