package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DimensModel(
    @field:Json(name = "meters") var meters: Double,
    @field:Json(name = "feet") var feet: Double
) : Parcelable