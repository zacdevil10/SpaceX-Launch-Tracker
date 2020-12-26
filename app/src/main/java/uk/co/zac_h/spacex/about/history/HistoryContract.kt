package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.model.spacex.History
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

interface HistoryContract {

    interface HistoryView {
        fun addHistory(history: ArrayList<HistoryHeaderModel>)
        fun openWebLink(link: String)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeProgress(isRefreshing: Boolean)
        fun showError(error: String)
    }

    interface HistoryPresenter {
        fun getHistory(sortNew: Boolean, api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface HistoryInteractor {
        fun getAllHistoricEvents(order: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(history: List<History>?)
        fun onError(error: String)
    }

}