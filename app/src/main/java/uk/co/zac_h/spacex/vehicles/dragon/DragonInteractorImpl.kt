package uk.co.zac_h.spacex.vehicles.dragon

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.Dragon
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class DragonInteractorImpl : BaseNetwork(), VehiclesContract.Interactor<Dragon> {

    private var call: Call<List<Dragon>>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<Dragon>
    ) {
        call = api.getDragons().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()

}