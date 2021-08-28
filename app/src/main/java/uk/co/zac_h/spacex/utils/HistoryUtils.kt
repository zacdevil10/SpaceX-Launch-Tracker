package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.dto.spacex.History
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

fun splitHistoryListByDate(history: List<History>): ArrayList<HistoryHeaderModel> {
    val newList = ArrayList<HistoryHeaderModel>()
    var year = 0
    history.forEach { model ->
        val newYear = model.event?.dateUtc?.subSequence(0, 4).toString().toInt()
        if (year != newYear) {
            newList.add(HistoryHeaderModel(newYear.toString(), null, true))
            year = newYear
        }
        newList.add(HistoryHeaderModel(null, model, false))
    }
    return newList
}