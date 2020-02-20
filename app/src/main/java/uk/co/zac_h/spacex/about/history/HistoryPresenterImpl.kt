package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.model.spacex.HistoryModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.splitHistoryListByDate

class HistoryPresenterImpl(
    private val view: HistoryView,
    private val interactor: HistoryInteractor
) : HistoryPresenter, HistoryInteractor.Callback {

    private val historyHeaders = ArrayList<HistoryHeaderModel>()

    override fun getHistory(api: SpaceXInterface) {
        view.showProgress()
        interactor.getAllHistoricEvents(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(history: List<HistoryModel>?) {
        history?.let {
            historyHeaders.splitHistoryListByDate(it)

            view.apply {
                hideProgress()
                addHistory(historyHeaders)
                toggleSwipeProgress(false)
            }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }


}