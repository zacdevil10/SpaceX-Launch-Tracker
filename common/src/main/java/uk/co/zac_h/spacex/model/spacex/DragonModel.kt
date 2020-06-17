package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class DragonModel(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "active") val active: Boolean,
    @field:Json(name = "crew_capacity") val crewCapacity: Int,
    @field:Json(name = "sidewall_angle_deg") val sidewallAngleDeg: Int,
    @field:Json(name = "orbit_duration_yr") val orbitDuration: Int,
    @field:Json(name = "dry_mass_kg") val dryMassKg: Int,
    @field:Json(name = "dry_mass_lb") val dryMassLb: Int,
    @field:Json(name = "first_flight") val firstFlight: String?,
    @field:Json(name = "heat_shield") val heatShield: HeatShieldModel,
    @field:Json(name = "thrusters") val thrusters: List<ThrusterConfigModel>,
    @field:Json(name = "launch_payload_mass") val launchPayloadMass: MassModel,
    @field:Json(name = "launch_payload_vol") val launchPayloadVolume: VolumeModel,
    @field:Json(name = "return_payload_mass") val returnPayloadMass: MassModel,
    @field:Json(name = "return_payload_vol") val returnPayloadVol: VolumeModel,
    @field:Json(name = "pressurized_capsule") val pressurizedCapsule: PressurizedCapsuleModel,
    @field:Json(name = "trunk") val trunk: TrunkModel,
    @field:Json(name = "height_w_trunk") val heightWithTrunk: DimensModel,
    @field:Json(name = "diameter") val diameter: DimensModel,
    @field:Json(name = "flickr_images") val flickr: List<String>,
    @field:Json(name = "wikipedia") val wikiLink: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "id") val id: String
) : Parcelable

