package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.model.spacex.HistoryModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface HistoryInteractor {

    fun getAllHistoricEvents(api: SpaceXInterface, listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(history: List<HistoryModel>?)
        fun onError(error: String)
    }
}