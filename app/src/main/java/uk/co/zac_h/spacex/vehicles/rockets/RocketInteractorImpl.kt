package uk.co.zac_h.spacex.vehicles.rockets

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Rocket
import uk.co.zac_h.spacex.model.spacex.RocketResponse
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class RocketInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Rocket>?> {

    private var call: Call<List<RocketResponse>>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Rocket>?>
    ) {
        call = api.getRockets().apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.map { Rocket(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}