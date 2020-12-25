package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class Thrust(
    @field:Json(name = "kN") val kN: Float?,
    @field:Json(name = "lbf") val lbf: Float?
) : Parcelable