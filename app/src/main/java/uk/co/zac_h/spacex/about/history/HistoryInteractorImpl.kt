package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class HistoryInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    HistoryInteractor {

    private var call = api.getHistory("desc")

    override fun getAllHistoricEvents(listener: HistoryInteractor.Callback) =
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }

    override fun cancelAllRequests() = call.cancel()
}