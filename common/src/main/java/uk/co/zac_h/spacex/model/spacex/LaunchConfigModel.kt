package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchConfigModel(
    @field:Json(name = "rocket_id") var id: String?,
    @field:Json(name = "rocket_name") var name: String?,
    @field:Json(name = "rocket_type") var type: String?,
    @field:Json(name = "first_stage") var firstStage: CoresListModel?,
    @field:Json(name = "second_stage") var secondStage: SecondStagePayloadListModel?,
    @field:Json(name = "fairings") var fairings: FairingsModel?
) : Parcelable

@Parcelize
data class CoresListModel(
    @field:Json(name = "cores") var cores: List<LaunchCoreModel>?
) : Parcelable

@Parcelize
data class SecondStagePayloadListModel(
    @field:Json(name = "block") var block: Int?,
    @field:Json(name = "payloads") var payloads: List<PayloadModel>?
) : Parcelable

@Parcelize
data class PayloadModel(
    @field:Json(name = "payload_id") var id: String?,
    @field:Json(name = "norad_id") var noradId: List<Int>?,
    @field:Json(name = "reused") var reused: Boolean?,
    @field:Json(name = "customers") var customers: List<String>?,
    @field:Json(name = "nationality") var nationality: String?,
    @field:Json(name = "manufacturer") var manufacturer: String?,
    @field:Json(name = "payload_type") var type: String?,
    @field:Json(name = "payload_mass_kg") var massKg: Float?,
    @field:Json(name = "payload_mass_lbs") var massLbs: Float?,
    @field:Json(name = "orbit") var orbit: String?,
    @field:Json(name = "orbit_params") var orbitParams: OrbitParamsModel?
) : Parcelable

@Parcelize
data class OrbitParamsModel(
    @field:Json(name = "reference_system") var referenceSystem: String?,
    @field:Json(name = "regime") var regime: String?,
    @field:Json(name = "longitude") var lng: Float?,
    @field:Json(name = "semi_major_axis_km") var semiMajorAxisKm: Float?,
    @field:Json(name = "eccentricity") var eccentricity: Float?,
    @field:Json(name = "periapsis_km") var periapsisKm: Float?,
    @field:Json(name = "apoapsis_km") var apoapsisKm: Float?,
    @field:Json(name = "inclination_deg") var inclination: Float?,
    @field:Json(name = "period_min") var periodMins: Float?,
    @field:Json(name = "lifespan_years") var lifespanYears: Float?,
    @field:Json(name = "epoch") var epoch: String?,
    @field:Json(name = "mean_motion") var meanMotion: Float?,
    @field:Json(name = "raan") var raan: Float?,
    @field:Json(name = "arg_of_pericenter") var pericenterArg: Float?,
    @field:Json(name = "mean_anomaly") var anomalyMean: Float?
) : Parcelable