package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class ThrusterConfigModel(
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "amount") val amount: Int?,
    @field:Json(name = "pods") val pods: Int?,
    @field:Json(name = "fuel_1") val fuelType1: String?,
    @field:Json(name = "fuel_2") val fuelType2: String?,
    @field:Json(name = "isp") val specificImpulse: Int?,
    @field:Json(name = "thrust") val thrust: ThrustModel?
) : Parcelable