package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.model.spacex.History
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

fun ArrayList<HistoryHeaderModel>.splitHistoryListByDate(history: List<History>) {
    this.clear()
    var year = 0
    history.forEach { model ->
        val newYear = model.event?.dateUtc?.subSequence(0, 4).toString().toInt()
        if (year != newYear) {
            this.add(HistoryHeaderModel(newYear.toString(), null, true))
            year = newYear
        }
        this.add(HistoryHeaderModel(null, model, false))
    }
}