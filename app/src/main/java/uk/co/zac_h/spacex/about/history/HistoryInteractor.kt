package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.model.HistoryModel

interface HistoryInteractor {

    fun getAllHistoricEvents(listener: HistoryInteractor.Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(history: List<HistoryModel>?)
        fun onError(error: String)
    }
}