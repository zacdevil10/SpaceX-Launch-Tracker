package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class SecondStageModel(
    @field:Json(name = "reusable") val reusable: Boolean,
    @field:Json(name = "engines") val engines: Int,
    @field:Json(name = "fuel_amount_tons") val fuelAmountTons: Double,
    @field:Json(name = "burn_time_sec") val burnTimeSec: Int,
    @field:Json(name = "thrust") val thrust: ThrustModel,
    @field:Json(name = "payloads") val payloads: PayloadConfigModel
) : Parcelable

@Parcelize
data class PayloadConfigModel(
    @field:Json(name = "option_1") val option1: String,
    @field:Json(name = "composite_fairing") val compositeFairing: FairingConfigModel
) : Parcelable

@Parcelize
data class FairingConfigModel(
    @field:Json(name = "height") val height: DimensModel,
    @field:Json(name = "diameter") val diameter: DimensModel
) : Parcelable