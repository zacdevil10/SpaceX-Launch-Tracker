package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

// v4
@Parcelize
data class Dragon(
    @field:Json(name = SPACEX_FIELD_DRAGON_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_DRAGON_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_DRAGON_ACTIVE) val active: Boolean?,
    @field:Json(name = SPACEX_FIELD_DRAGON_CREW_CAPACITY) val crewCapacity: Int?,
    @field:Json(name = SPACEX_FIELD_DRAGON_SIDEWALL_ANGLE_DEG) val sidewallAngleDeg: Int?,
    @field:Json(name = SPACEX_FIELD_DRAGON_ORBIT_DURATION_YR) val orbitDuration: Int?,
    @field:Json(name = SPACEX_FIELD_DRAGON_DRY_MASS_KG) val dryMassKg: Float?,
    @field:Json(name = SPACEX_FIELD_DRAGON_DRY_MASS_LB) val dryMassLb: Float?,
    @field:Json(name = SPACEX_FIELD_DRAGON_FIRST_FLIGHT) val firstFlight: String?,
    @field:Json(name = SPACEX_FIELD_DRAGON_HEAT_SHIELD) val heatShield: HeatShieldModel?,
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS) val thrusters: List<DragonThrusterConfiguration>?,
    @field:Json(name = SPACEX_FIELD_DRAGON_LAUNCH_PAYLOAD_MASS) val launchPayloadMass: Mass?,
    @field:Json(name = SPACEX_FIELD_DRAGON_LAUNCH_PAYLOAD_VOL) val launchPayloadVolume: Volume?,
    @field:Json(name = SPACEX_FIELD_DRAGON_RETURN_PAYLOAD_MASS) val returnPayloadMass: Mass?,
    @field:Json(name = SPACEX_FIELD_DRAGON_RETURN_PAYLOAD_VOL) val returnPayloadVol: Volume?,
    @field:Json(name = SPACEX_FIELD_DRAGON_PRESSURIZED_CAPSULE) val pressurizedCapsule: PressurizedCapsule?,
    @field:Json(name = SPACEX_FIELD_DRAGON_TRUNK) val trunk: Trunk?,
    @field:Json(name = SPACEX_FIELD_DRAGON_HEIGHT_W_TRUNK) val heightWithTrunk: Dimens?,
    @field:Json(name = SPACEX_FIELD_DRAGON_DIAMETER) val diameter: Dimens?,
    @field:Json(name = SPACEX_FIELD_DRAGON_FLICKR_IMAGES) val flickr: List<String>?,
    @field:Json(name = SPACEX_FIELD_DRAGON_WIKIPEDIA) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_DRAGON_DESCRIPTION) val description: String?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
) : Parcelable

@Parcelize
data class HeatShieldModel(
    @field:Json(name = SPACEX_FIELD_DRAGON_MATERIAL) val material: String,
    @field:Json(name = SPACEX_FIELD_DRAGON_SIZE_METERS) val size: Float,
    @field:Json(name = SPACEX_FIELD_DRAGON_TEMP_DEGREES) val temp: Int,
    @field:Json(name = SPACEX_FIELD_DRAGON_DEV_PARTNER) val developmentPartner: String
) : Parcelable

@Parcelize
data class DragonThrusterConfiguration(
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS_TYPE) val type: String,
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS_AMOUNT) val amount: Int,
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS_PODS) val pods: Int,
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS_FUEL_1) val fuelType1: String,
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS_FUEL_2) val fuelType2: String,
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS_ISP) val specificImpulse: Int,
    @field:Json(name = SPACEX_FIELD_DRAGON_THRUSTERS_THRUST) val thrust: Thrust
) : Parcelable

@Parcelize
data class PressurizedCapsule(
    @field:Json(name = SPACEX_FIELD_DRAGON_PAYLOAD_VOLUME) val payloadVolume: Volume
) : Parcelable

@Parcelize
data class Trunk(
    @field:Json(name = SPACEX_FIELD_DRAGON_TRUNK_VOLUME) val trunkVolume: Volume,
    @field:Json(name = SPACEX_FIELD_DRAGON_CARGO) val cargo: Cargo
) : Parcelable

@Parcelize
data class Cargo(
    @field:Json(name = SPACEX_FIELD_DRAGON_SOLAR_ARRAY) val solarArray: Int?,
    @field:Json(name = SPACEX_FIELD_DRAGON_UNPRESSURIZED_CARGO) val unpressurizedCargo: Boolean?
) : Parcelable