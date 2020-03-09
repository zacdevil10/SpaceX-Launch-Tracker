package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ThrustModel(
    @field:Json(name = "kN") var kN: Float,
    @field:Json(name = "lbf") var lbf: Int
) : Parcelable