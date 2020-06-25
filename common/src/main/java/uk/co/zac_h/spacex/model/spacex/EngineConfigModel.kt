package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class EngineConfigModel(
    @field:Json(name = "number") val number: Int?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "version") val version: String?,
    @field:Json(name = "layout") val layout: String?,
    @field:Json(name = "isp") val specificImpulse: SpecificImpulseModel?,
    @field:Json(name = "engine_loss_max") val engine_loss_max: Int?,
    @field:Json(name = "propellant_1") val propellant_1: String?,
    @field:Json(name = "propellant_2") val propellant_2: String?,
    @field:Json(name = "thrust_sea_level") val thrust_sea_level: ThrustModel?,
    @field:Json(name = "thrust_vacuum") val thrust_vacuum: ThrustModel?,
    @field:Json(name = "thrust_to_weight") val thrust_to_weight: Double?
) : Parcelable

