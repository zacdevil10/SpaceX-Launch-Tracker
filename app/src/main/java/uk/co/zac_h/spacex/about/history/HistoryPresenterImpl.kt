package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.model.HistoryModel
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

class HistoryPresenterImpl(
    private val view: HistoryView,
    private val interactor: HistoryInteractor
) : HistoryPresenter, HistoryInteractor.Callback {

    private val historyHeaders = ArrayList<HistoryHeaderModel>()

    private var year: Int = 0

    override fun getHistory() {
        view.showProgress()
        interactor.getAllHistoricEvents(this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(history: List<HistoryModel>?) {
        history?.let {
            historyHeaders.clear()
            it.forEach { model ->
                val newYear = model.dateUtc.subSequence(0, 4).toString().toInt()
                if (year != newYear) {
                    historyHeaders.add(
                        HistoryHeaderModel(
                            newYear.toString(),
                            null,
                            true
                        )
                    )
                    year = newYear
                }
                historyHeaders.add(
                    HistoryHeaderModel(
                        null,
                        model,
                        false
                    )
                )
            }

            view.apply {
                hideProgress()
                updateRecycler(historyHeaders)
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