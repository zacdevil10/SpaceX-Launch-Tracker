package uk.co.zac_h.spacex.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RocketsModel(
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "active") var active: Boolean,
    @field:Json(name = "stages") var stages: Int,
    @field:Json(name = "boosters") var boosters: Int,
    @field:Json(name = "cost_per_launch") var costPerLaunch: Int,
    @field:Json(name = "success_rate_pct") var successRate: Int,
    @field:Json(name = "first_flight") var firstFlight: String,
    @field:Json(name = "country") var country: String,
    @field:Json(name = "company") var company: String,
    @field:Json(name = "height") var height: RocketDimensModel,
    @field:Json(name = "diameter") var diameter: RocketDimensModel,
    @field:Json(name = "mass") var mass: RocketMassModel,
    @field:Json(name = "payload_weights") var payloadWeightsList: List<PayloadWeightsModel>,
    @field:Json(name = "first_stage") var firstStage: FirstStageModel,
    @field:Json(name = "second_stage") var secondStage: SecondStageModel,
    @field:Json(name = "engines") var engines: EngineConfigModel,
    @field:Json(name = "landing_legs") var landingLegs: LandingLegsModel,
    @field:Json(name = "wikipedia") var wikipedia: String,
    @field:Json(name = "description") var description: String,
    @field:Json(name = "rocket_id") var rocketId: String,
    @field:Json(name = "rocket_name") var rocketName: String,
    @field:Json(name = "rocket_type") var rocketType: String
) : Parcelable

@Parcelize
data class RocketDimensModel(
    @field:Json(name = "meters") var meters: Double,
    @field:Json(name = "feet") var feet: Double
) : Parcelable

@Parcelize
data class RocketMassModel(
    @field:Json(name = "kg") var kg: Int,
    @field:Json(name = "lb") var lb: Int
) : Parcelable

@Parcelize
data class PayloadWeightsModel(
    @field:Json(name = "id") var id: String,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "kg") var kg: Int,
    @field:Json(name = "lb") var lb: Int
) : Parcelable

@Parcelize
data class FirstStageModel(
    @field:Json(name = "reusable") var reusable: Boolean,
    @field:Json(name = "engines") var engines: Int,
    @field:Json(name = "fuel_amount_tons") var fuelAmountTons: Double,
    @field:Json(name = "burn_time_sec") var burnTimeSec: Int?,
    @field:Json(name = "thrust_sea_level") var thrustSeaLevel: ThrustModel,
    @field:Json(name = "thrust_vacuum") var thrustVacuum: ThrustModel
) : Parcelable

@Parcelize
data class SecondStageModel(
    @field:Json(name = "engines") var engines: Int,
    @field:Json(name = "fuel_amount_tons") var fuelAmountTons: Double,
    @field:Json(name = "burn_time_sec") var burnTimeSec: Int?,
    @field:Json(name = "thrust") var thrust: ThrustModel
) : Parcelable

@Parcelize
data class ThrustModel(
    @field:Json(name = "kN") var kN: Int,
    @field:Json(name = "lbf") var lbf: Int
) : Parcelable

@Parcelize
data class EngineConfigModel(
    @field:Json(name = "number") var number: Int,
    @field:Json(name = "type") var type: String,
    @field:Json(name = "version") var version: String,
    @field:Json(name = "layout") var layout: String?,
    @field:Json(name = "engine_loss_max") var engine_loss_max: Int?,
    @field:Json(name = "propellant_1") var propellant_1: String,
    @field:Json(name = "propellant_2") var propellant_2: String,
    @field:Json(name = "thrust_sea_level") var thrust_sea_level: ThrustModel,
    @field:Json(name = "thrust_vacuum") var thrust_vacuum: ThrustModel,
    @field:Json(name = "thrust_to_weight") var thrust_to_weight: Double?
) : Parcelable

@Parcelize
data class LandingLegsModel(
    @field:Json(name = "number") var quantity: Int,
    @field:Json(name = "material") var material: String?
) : Parcelable