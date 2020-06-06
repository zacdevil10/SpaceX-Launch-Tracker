package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchCoreModel(
    @field:Json(name = "core_serial") var serial: String?,
    @field:Json(name = "flight") var flight: Int?,
    @field:Json(name = "block") var block: Int?,
    @field:Json(name = "gridfins") var gridfins: Boolean?,
    @field:Json(name = "legs") var legs: Boolean?,
    @field:Json(name = "reused") var reused: Boolean?,
    @field:Json(name = "land_success") var landingSuccess: Boolean?,
    @field:Json(name = "landing_intent") var landingIntent: Boolean?,
    @field:Json(name = "landing_type") var landingType: String?,
    @field:Json(name = "landing_vehicle") var landingVehicle: String?
) : Parcelable