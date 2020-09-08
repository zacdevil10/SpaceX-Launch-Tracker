package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class HeatShieldModel(
    @field:Json(name = "material") val material: String?,
    @field:Json(name = "size_meters") val size: Float?,
    @field:Json(name = "temp_degrees") val temp: Int?,
    @field:Json(name = "dev_partner") val developmentPartner: String?
) : Parcelable