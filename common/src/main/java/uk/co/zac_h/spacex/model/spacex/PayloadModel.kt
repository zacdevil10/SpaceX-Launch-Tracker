package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class PayloadModel(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "reused") val reused: Boolean,
    @field:Json(name = "launch") val launch: String?,
    @field:Json(name = "customers") val customers: List<String>,
    @field:Json(name = "norad_ids") val noradIds: List<Int>,
    @field:Json(name = "nationalities") val nationalities: List<String>,
    @field:Json(name = "manufacturers") val manufacturers: List<String>,
    @field:Json(name = "mass_kg") val massKg: Float?,
    @field:Json(name = "mass_lbs") val massLbs: Float?,
    @field:Json(name = "orbit") val orbit: String?,
    @field:Json(name = "reference_system") val referenceSystem: String?,
    @field:Json(name = "regime") val regime: String?,
    @field:Json(name = "longitude") val longitude: Float?,
    @field:Json(name = "semi_major_axis_km") val semiMajorAxisKm: Float?,
    @field:Json(name = "eccentricity") val eccentricity: Float?,
    @field:Json(name = "periapsis_km") val periapsisKm: Float?,
    @field:Json(name = "apoapsis_km") val apoapsisKm: Float?,
    @field:Json(name = "inclination_deg") val inclination: Float?,
    @field:Json(name = "period_min") val period: Float?,
    @field:Json(name = "lifespan_years") val lifespan: Int?,
    @field:Json(name = "epoch") val epoch: String?,
    @field:Json(name = "mean_motion") val meanMotion: Float?,
    @field:Json(name = "raan") val raan: Float?,
    @field:Json(name = "arg_of_pericenter") val argOfPericenter: Float?,
    @field:Json(name = "mean_anomaly") val meanAnomaly: Float?,
    @field:Json(name = "dragon") val dragon: PayloadDragonModel,
    @field:Json(name = "id") val id: String
) : Parcelable

@Parcelize
data class PayloadDragonModel(
    @field:Json(name = "capsule") val capsule: String?,
    @field:Json(name = "mass_returned_kg") val massReturnedKg: Float?,
    @field:Json(name = "mass_returned_lbs") val massReturnedLbs: Float?,
    @field:Json(name = "flight_time_sec") val flightTime: Int?,
    @field:Json(name = "manifest") val manifest: String?,
    @field:Json(name = "water_landing") val waterLanding: Boolean?,
    @field:Json(name = "land_landing") val landLanding: Boolean?
) : Parcelable