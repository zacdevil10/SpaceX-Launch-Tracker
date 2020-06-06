package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FairingsModel(
    @field:Json(name = "reused") var reused: Boolean?,
    @field:Json(name = "recovery_attempt") var recoveryAttempt: Boolean?,
    @field:Json(name = "recovered") var isRecovered: Boolean?,
    @field:Json(name = "ships") var ships: List<String>?
) : Parcelable