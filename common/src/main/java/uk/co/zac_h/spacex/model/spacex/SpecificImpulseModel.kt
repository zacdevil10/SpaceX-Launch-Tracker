package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpecificImpulseModel(
    @field:Json(name = "sea_level") val seaLevel: Int?,
    @field:Json(name = "vacuum") val vacuum: Int?
) : Parcelable