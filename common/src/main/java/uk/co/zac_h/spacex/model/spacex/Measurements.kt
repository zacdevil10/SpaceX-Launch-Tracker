package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dimens(
    @field:Json(name = "meters") val meters: Double?,
    @field:Json(name = "feet") val feet: Double?
) : Parcelable

@Parcelize
data class Mass(
    @field:Json(name = "kg") var kg: Float?,
    @field:Json(name = "lb") var lb: Float?
) : Parcelable

@Parcelize
data class Volume(
    @field:Json(name = "cubic_meters") val cubicMeters: Int?,
    @field:Json(name = "cubic_feet") val cubicFeet: Int?
) : Parcelable

@Parcelize
data class Thrust(
    @field:Json(name = "kN") val kN: Float?,
    @field:Json(name = "lbf") val lbf: Float?
) : Parcelable

@Parcelize
data class SpecificImpulse(
    @field:Json(name = "sea_level") val seaLevel: Int?,
    @field:Json(name = "vacuum") val vacuum: Int?
) : Parcelable

@Parcelize
data class Velocity(
    val kph: Int? = null,
    val mph: Int? = null
) : Parcelable

@Parcelize
data class Distance(
    val meters: String? = null,
    val feet: String? = null,
    val mi: String? = null,
    val km: String? = null
) : Parcelable