package uk.co.zac_h.spacex.dashboard

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DashboardInteractorImpl : BaseNetwork(), DashboardInteractor {

    private lateinit var call: Call<LaunchesModel>

    private val active = ArrayList<String>()

    override fun getSingleLaunch(
        id: String,
        api: SpaceXInterface,
        listener: DashboardInteractor.InteractorCallback
    ) {
        call = api.getSingleLaunch(id).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(id, it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun hasActiveRequest(): Boolean = active.isNotEmpty()

    override fun cancelAllRequests() {
        if (::call.isInitialized) call.cancel()
    }
}