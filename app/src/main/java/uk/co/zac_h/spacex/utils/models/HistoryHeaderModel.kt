package uk.co.zac_h.spacex.utils.models

import uk.co.zac_h.spacex.model.HistoryModel

data class HistoryHeaderModel(
    val header: String?,
    val historyModel: HistoryModel?,
    val isHeader: Boolean
)