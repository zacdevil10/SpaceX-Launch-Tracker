package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FirstStageModel(
    @field:Json(name = "thrust_sea_level") val thrustSeaLevel: ThrustModel?,
    @field:Json(name = "thrust_vacuum") val thrustVacuum: ThrustModel?,
    @field:Json(name = "reusable") val reusable: Boolean?,
    @field:Json(name = "engines") val engines: Int?,
    @field:Json(name = "fuel_amount_tons") val fuelAmountTons: Double?,
    @field:Json(name = "burn_time_sec") val burnTimeSec: Int?
) : Parcelable