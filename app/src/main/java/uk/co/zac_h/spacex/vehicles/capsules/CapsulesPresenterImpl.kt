package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.model.spacex.Capsule
import uk.co.zac_h.spacex.model.spacex.CapsuleQueriedResponse
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class CapsulesPresenterImpl(
    private val view: VehiclesContract.View<Capsule>,
    private val interactor: VehiclesContract.Interactor<CapsuleQueriedResponse>
) : VehiclesContract.Presenter, VehiclesContract.InteractorCallback<CapsuleQueriedResponse> {

    override fun getVehicles(api: SpaceXInterface) {
        view.showProgress()
        interactor.getVehicles(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(vehicles: List<CapsuleQueriedResponse>?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
            vehicles?.map { Capsule(it) }?.let { updateVehicles(it) }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}