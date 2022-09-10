package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

data class RocketResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ACTIVE) val isActive: Boolean?,
    @field:Json(name = SPACEX_FIELD_ROCKET_STAGES) val stages: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_BOOSTERS) val boosters: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_COST_PER_LAUNCH) val costPerLaunch: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SUCCESS_RATE_PCT) val successRate: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_FLIGHT) val firstFlight: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_COUNTRY) val country: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_COMPANY) val company: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_HEIGHT) val height: Dimens?,
    @field:Json(name = SPACEX_FIELD_ROCKET_DIAMETER) val diameter: Dimens?,
    @field:Json(name = SPACEX_FIELD_ROCKET_MASS) val mass: Mass?,
    @field:Json(name = SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS) val payloadWeights: List<PayloadWeightsResponse>?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE) val firstStage: FirstStageResponse?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE) val secondStage: SecondStageResponse?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES) val engines: EngineConfigModel?,
    @field:Json(name = SPACEX_FIELD_ROCKET_LANDING_LEGS) val landingLegs: LandingLegsModel?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FLICKR) val flickr: List<String>?,
    @field:Json(name = SPACEX_FIELD_ROCKET_WIKIPEDIA) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_DESCRIPTION) val description: String?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class PayloadWeightsResponse(
    @field:Json(name = SPACEX_FIELD_ID) val id: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS_KG) val kg: Float?,
    @field:Json(name = SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS_LB) val lb: Float?
)

data class FirstStageResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_REUSABLE) val reusable: Boolean?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_ENGINES) val engines: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_FUEL_AMOUNT_TONS) val fuelAmountTons: Double?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_BURN_TIME_SEC) val burnTimeSec: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_THRUST_SEA_LEVEL) val thrustSeaLevel: Thrust?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_THRUST_VACUUM) val thrustVacuum: Thrust?
)

data class SecondStageResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_REUSABLE) val reusable: Boolean?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_ENGINES) val engines: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_FUEL_AMOUNT_TONS) val fuelAmountTons: Double?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_BURN_TIME_SEC) val burnTimeSec: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_THRUST) val thrust: Thrust?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOADS) val payloads: PayloadConfigResponse?
)

data class PayloadConfigResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_OPTION_1) val option1: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_COMPOSITE_FAIRING) val compositeFairing: FairingConfigResponse?
)

data class FairingConfigResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_FAIRING_HEIGHT) val height: Dimens?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_FAIRING_DIAMETER) val diameter: Dimens?
)

data class EngineConfigModel(
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_NUMBER) val number: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_VERSION) val version: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_LAYOUT) val layout: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_ISP) val specificImpulse: SpecificImpulse?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_ENGINE_LOSS_MAX) val engine_loss_max: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_PROPELLANT_1) val propellant_1: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_PROPELLANT_2) val propellant_2: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_THRUST_SEA_LEVEL) val thrust_sea_level: Thrust?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_THRUST_VACUUM) val thrust_vacuum: Thrust?,
    @field:Json(name = SPACEX_FIELD_ROCKET_ENGINES_THRUST_TO_WEIGHT) val thrust_to_weight: Double?
)

data class LandingLegsModel(
    @field:Json(name = SPACEX_FIELD_ROCKET_LANDING_LEGS_NUMBER) val quantity: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_LANDING_LEGS_MATERIAL) val material: String?
)