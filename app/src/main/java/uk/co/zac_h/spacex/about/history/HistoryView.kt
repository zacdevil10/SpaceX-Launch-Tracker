package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

interface HistoryView {

    fun updateRecycler(history: ArrayList<HistoryHeaderModel>)

    fun openWebLink(link: String)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)

}