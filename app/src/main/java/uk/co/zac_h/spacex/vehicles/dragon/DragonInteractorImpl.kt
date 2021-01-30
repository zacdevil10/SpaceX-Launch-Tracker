package uk.co.zac_h.spacex.vehicles.dragon

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Dragon
import uk.co.zac_h.spacex.model.spacex.DragonResponse
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DragonInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Dragon>?> {

    private var call: Call<List<DragonResponse>>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Dragon>?>
    ) {
        call = api.getDragons().apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.map { Dragon(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()

}