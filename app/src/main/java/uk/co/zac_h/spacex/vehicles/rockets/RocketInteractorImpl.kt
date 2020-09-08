package uk.co.zac_h.spacex.vehicles.rockets

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class RocketInteractorImpl : BaseNetwork(),
    VehiclesContract.Interactor<RocketsModel> {

    private var call: Call<List<RocketsModel>>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<RocketsModel>
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