package uk.co.zac_h.spacex.about.history

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.HistoryModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class HistoryInteractorImpl : BaseNetwork(), HistoryContract.HistoryInteractor {

    private var call: Call<List<HistoryModel>>? = null

    override fun getAllHistoricEvents(
        api: SpaceXInterface,
        listener: HistoryContract.InteractorCallback
    ) {
        call = api.getHistory("desc").apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = call?.cancel()
}