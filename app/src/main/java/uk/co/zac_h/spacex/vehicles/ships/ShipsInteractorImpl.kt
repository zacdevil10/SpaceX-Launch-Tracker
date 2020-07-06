package uk.co.zac_h.spacex.vehicles.ships

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.ShipModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class ShipsInteractorImpl : BaseNetwork(), VehiclesContract.Interactor<ShipModel> {

    private var call: Call<List<ShipModel>>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<ShipModel>
    ) {
        call = api.getShips().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}