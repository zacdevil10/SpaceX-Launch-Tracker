package uk.co.zac_h.spacex.utils.models

import uk.co.zac_h.spacex.dto.spacex.History

data class HistoryHeaderModel(
    val header: String?,
    val historyModel: History?,
    val isHeader: Boolean
)