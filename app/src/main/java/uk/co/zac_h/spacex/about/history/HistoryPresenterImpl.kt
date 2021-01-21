package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.History
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.splitHistoryListByDate

class HistoryPresenterImpl(
    private val view: HistoryView,
    private val interactor: NetworkInterface.Interactor<List<History>?>
) : NetworkInterface.Presenter<Nothing>, NetworkInterface.Callback<List<History>?> {

    private val historyHeaders = ArrayList<HistoryHeaderModel>()

    override fun get(data: Any, api: SpaceXInterface) {
        view.showProgress()
        interactor.get(if (data as Boolean) "desc" else "asc", api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<History>?) {
        response?.let {
            historyHeaders.splitHistoryListByDate(it)

            view.apply {
                hideProgress()
                update(historyHeaders)
                toggleSwipeRefresh(false)
            }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }


}