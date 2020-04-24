package uk.co.zac_h.spacex.vehicles.dragon

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.DragonModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DragonInteractorImpl : BaseNetwork(), DragonContract.DragonInteractor {

    private var call: Call<List<DragonModel>>? = null

    override fun getDragon(api: SpaceXInterface, listener: DragonContract.InteractorCallback) {
        call = api.getDragons().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelRequest() = terminateAll()

}