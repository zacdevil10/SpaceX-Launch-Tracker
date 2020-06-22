package uk.co.zac_h.spacex.dashboard

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DashboardInteractorImpl : BaseNetwork(), DashboardContract.DashboardInteractor {

    private var call: Call<LaunchesModel>? = null

    private val active = ArrayList<String>()

    override fun getSingleLaunch(
        id: String,
        api: SpaceXInterface,
        listener: DashboardContract.InteractorCallback
    ) {
        active.add(id)
        call = api.getSingleLaunch(id).apply {
            makeCall {
                onResponseSuccess = {
                    listener.onSuccess(id, it.body())
                    active.remove(id)
                }
                onResponseFailure = {
                    listener.onError(it)
                    active.remove(id)
                }
            }
        }
    }

    override fun hasActiveRequest(): Boolean = active.isNotEmpty()

    override fun cancelAllRequests() = terminateAll()
}