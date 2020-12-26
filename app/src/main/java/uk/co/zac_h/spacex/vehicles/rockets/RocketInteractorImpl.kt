package uk.co.zac_h.spacex.vehicles.rockets

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.Rocket
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class RocketInteractorImpl : BaseNetwork(),
    VehiclesContract.Interactor<Rocket> {

    private var call: Call<List<Rocket>>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<Rocket>
    ) {
        call = api.getRockets().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}