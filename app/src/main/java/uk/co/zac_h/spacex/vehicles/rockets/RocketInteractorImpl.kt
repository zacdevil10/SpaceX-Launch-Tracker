package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Rocket
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class RocketInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Rocket>?> {

    //private var call: Call<List<RocketResponse>>? = null

    override fun get(
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<Rocket>?>
    ) {
        /*call = api.getRockets().apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.map { Rocket(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}