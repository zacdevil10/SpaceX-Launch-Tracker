package uk.co.zac_h.spacex.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.model.spacex.HistoryModel

@Parcelize
data class HistoryHeaderModel(
    val header: String?,
    val historyModel: HistoryModel?,
    val isHeader: Boolean
) : Parcelable