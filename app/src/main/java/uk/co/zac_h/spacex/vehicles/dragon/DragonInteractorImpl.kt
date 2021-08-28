package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Dragon
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class DragonInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Dragon>?> {

    //private var call: Call<List<DragonResponse>>? = null

    override fun get(
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<Dragon>?>
    ) {
        /*call = api.getDragons().apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.map { Dragon(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()

}