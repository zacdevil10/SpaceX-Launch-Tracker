package uk.co.zac_h.spacex.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryStatsModel(
    val name: String,
    var successes: Int = 0,
    var failures: Int = 0,
    var successRate: Int = 0
) : Parcelable