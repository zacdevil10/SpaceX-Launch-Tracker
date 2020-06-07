package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SecondStageModel(
    @field:Json(name = "thrust") val thrust: ThrustModel?,
    @field:Json(name = "reusable") val reusable: Boolean?,
    @field:Json(name = "engines") val engines: Int?,
    @field:Json(name = "fuel_amount_tons") val fuelAmountTons: Double?,
    @field:Json(name = "burn_time_sec") val burnTimeSec: Int?
) : Parcelable