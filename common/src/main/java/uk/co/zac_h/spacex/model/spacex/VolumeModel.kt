package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class VolumeModel(
    @field:Json(name = "cubic_meters") val cubicMeters: Int,
    @field:Json(name = "cubic_feet") val cubicFeet: Int
) : Parcelable
