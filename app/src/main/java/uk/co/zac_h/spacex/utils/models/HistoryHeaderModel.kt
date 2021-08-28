package uk.co.zac_h.spacex.utils.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uk.co.zac_h.spacex.dto.spacex.History

data class HistoryHeaderModel(
    val header: String?,
    val historyModel: History?,
    val isHeader: Boolean
)