package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*
import java.text.DecimalFormat

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

@Parcelize
data class FirstStageResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_REUSABLE) val reusable: Boolean?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_ENGINES) val engines: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_FUEL_AMOUNT_TONS) val fuelAmountTons: Double?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_BURN_TIME_SEC) val burnTimeSec: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_THRUST_SEA_LEVEL) val thrustSeaLevel: Thrust?,
    @field:Json(name = SPACEX_FIELD_ROCKET_FIRST_STAGE_THRUST_VACUUM) val thrustVacuum: Thrust?
) : Parcelable

@Parcelize
data class SecondStageResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_REUSABLE) val reusable: Boolean?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_ENGINES) val engines: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_FUEL_AMOUNT_TONS) val fuelAmountTons: Double?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_BURN_TIME_SEC) val burnTimeSec: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_THRUST) val thrust: Thrust?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOADS) val payloads: PayloadConfigResponse?
) : Parcelable

@Parcelize
data class PayloadConfigResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_OPTION_1) val option1: String?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_COMPOSITE_FAIRING) val compositeFairing: FairingConfigResponse?
) : Parcelable

@Parcelize
data class FairingConfigResponse(
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_FAIRING_HEIGHT) val height: Dimens?,
    @field:Json(name = SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_FAIRING_DIAMETER) val diameter: Dimens?
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class LandingLegsModel(
    @field:Json(name = SPACEX_FIELD_ROCKET_LANDING_LEGS_NUMBER) val quantity: Int?,
    @field:Json(name = SPACEX_FIELD_ROCKET_LANDING_LEGS_MATERIAL) val material: String?
) : Parcelable

@Parcelize
data class Rocket(
    val name: String?,
    val type: String?,
    val isActive: Boolean?,
    val stages: Int?,
    val boosters: Int?,
    val costPerLaunch: String?,
    val successRate: Int?,
    val firstFlight: String?,
    val country: String?,
    val company: String?,
    val height: Dimens?,
    val diameter: Dimens?,
    val mass: Mass?,
    val payloadWeights: List<PayloadWeights>?,
    val firstStage: FirstStageResponse?,
    val secondStage: SecondStageResponse?,
    val engines: EngineConfigModel?,
    val landingLegs: LandingLegsModel?,
    val flickr: List<String>?,
    val wikipedia: String?,
    val description: String?,
    val id: String
) : Parcelable {

    constructor(
        response: RocketResponse
    ) : this(
        name = response.name,
        type = response.type,
        isActive = response.isActive,
        stages = response.stages,
        boosters = response.boosters,
        costPerLaunch = DecimalFormat("$#,###.00").format(response.costPerLaunch),
        successRate = response.successRate,
        firstFlight = response.firstFlight,
        country = response.country,
        company = response.company,
        height = response.height,
        diameter = response.diameter,
        mass = response.mass,
        payloadWeights = response.payloadWeights?.map { PayloadWeights(it) },
        firstStage = response.firstStage,
        secondStage = response.secondStage,
        engines = response.engines,
        landingLegs = response.landingLegs,
        flickr = response.flickr,
        wikipedia = response.wikipedia,
        description = response.description,
        id = response.id
    )

}

@Parcelize
data class PayloadWeights(
    val id: String?,
    val name: String?,
    val mass: Mass?
) : Parcelable {

    constructor(
        response: PayloadWeightsResponse
    ) : this(
        id = response.id,
        name = response.name,
        mass = Mass(kg = response.kg, lb = response.lb)
    )

}