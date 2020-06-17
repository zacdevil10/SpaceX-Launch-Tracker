package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class FairingsModel(
    @field:Json(name = "reused") val reused: Boolean?,
    @field:Json(name = "recovery_attempt") val recoveryAttempt: Boolean?,
    @field:Json(name = "recovered") val isRecovered: Boolean?,
    @field:Json(name = "ships") val ships: List<String>
) : Parcelable