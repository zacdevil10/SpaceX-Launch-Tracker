package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.model.spacex.HistoryModel
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

fun ArrayList<HistoryHeaderModel>.splitHistoryListByDate(it: List<HistoryModel>) {
    this.clear()
    var year = 0
    it.forEach { model ->
        val newYear = model.dateUtc.subSequence(0, 4).toString().toInt()
        if (year != newYear) {
            this.add(
                HistoryHeaderModel(
                    newYear.toString(),
                    null,
                    true
                )
            )
            year = newYear
        }
        this.add(
            HistoryHeaderModel(
                null,
                model,
                false
            )
        )
    }
}