package uk.co.zac_h.spacex.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.RocketType

@Parcelize
data class HistoryStatsModel(
    val rocket: RocketType,
    var successes: Int = 0,
    var failures: Int = 0,
    var successRate: Int = 0
) : Parcelable