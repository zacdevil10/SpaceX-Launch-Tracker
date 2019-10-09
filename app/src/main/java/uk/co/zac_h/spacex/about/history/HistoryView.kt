package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.utils.HistoryHeaderModel

interface HistoryView {

    fun updateRecycler(history: ArrayList<HistoryHeaderModel>)

}