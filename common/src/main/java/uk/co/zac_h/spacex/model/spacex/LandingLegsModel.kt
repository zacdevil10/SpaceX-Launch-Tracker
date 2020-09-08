package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class LandingLegsModel(
    @field:Json(name = "number") val quantity: Int?,
    @field:Json(name = "material") val material: String?
) : Parcelable