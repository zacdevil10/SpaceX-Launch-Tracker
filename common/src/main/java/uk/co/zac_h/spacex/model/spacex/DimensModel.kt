package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class DimensModel(
    @field:Json(name = "meters") val meters: Double,
    @field:Json(name = "feet") val feet: Double
) : Parcelable