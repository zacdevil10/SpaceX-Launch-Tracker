package uk.co.zac_h.spacex.dashboard

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.model.spacex.LaunchResponse
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DashboardWearInteractorImpl : BaseNetwork(), DashboardWearInteractor {

    private var call: Call<LaunchResponse>? = null

    override fun getSingleLaunch(id: String, listener: DashboardWearInteractor.Callback) {
        call = SpaceXInterface.create().getLaunch(id).apply {
            makeCall {
                onResponseSuccess = {
                    when (id) {
                        "next" -> listener.onNextSuccess(it.body()?.let { it1 -> Launch(it1) })
                        "latest" -> listener.onLatestSuccess(it.body()?.let { it1 -> Launch(it1) })
                    }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = call?.cancel()
}