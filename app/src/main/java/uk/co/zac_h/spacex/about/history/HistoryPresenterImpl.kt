package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.History
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.Keys.HistoryKeys
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.splitHistoryListByDate

class HistoryPresenterImpl(
    private val view: NetworkInterface.View<ArrayList<HistoryHeaderModel>>,
    private val interactor: NetworkInterface.Interactor<List<History>>,
    private val orderSharedPreferences: OrderSharedPreferencesHelper
) : HistoryContract.Presenter, NetworkInterface.Callback<List<History>> {

    private val historyHeaders = ArrayList<HistoryHeaderModel>()

    override fun get(api: SpaceXInterface) {
        view.showProgress()
        interactor.get(
            if (getOrder()) HistoryKeys.ORDER_DESCENDING else HistoryKeys.ORDER_ASCENDING,
            api,
            this
        )
    }

    override fun getOrUpdate(response: ArrayList<HistoryHeaderModel>?, api: SpaceXInterface) {
        response?.let {
            updateView(it)
        } ?: super.getOrUpdate(response, api)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: List<History>) {
        historyHeaders.splitHistoryListByDate(response)
        updateView(historyHeaders)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }

    private fun updateView(response: ArrayList<HistoryHeaderModel>) {
        view.apply {
            hideProgress()
            update(response)
            toggleSwipeRefresh(false)
        }
    }

    override fun getOrder(): Boolean = orderSharedPreferences.isSortedNew(HistoryKeys.HISTORY_ORDER)

    override fun setOrder(order: Boolean) {
        orderSharedPreferences.setSortOrder(HistoryKeys.HISTORY_ORDER, order)
    }
}