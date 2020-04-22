package uk.co.zac_h.spacex.dashboard

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DashboardWearInteractorImpl : BaseNetwork(), DashboardWearInteractor {

    private var call: Call<LaunchesModel>? = null

    override fun getSingleLaunch(id: String, listener: DashboardWearInteractor.Callback) {
        call = SpaceXInterface.create().getSingleLaunch(id).apply {
            makeCall {
                onResponseSuccess = {
                    when (id) {
                        "next" -> listener.onNextSuccess(it.body())
                        "latest" -> listener.onLatestSuccess(it.body())
                    }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = call?.cancel()
}