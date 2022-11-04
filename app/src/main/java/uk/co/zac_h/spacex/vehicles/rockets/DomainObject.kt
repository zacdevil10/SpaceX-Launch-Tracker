package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.network.dto.spacex.*
import java.text.DecimalFormat

data class Rocket(
    val name: String?,
    val type: RocketType?,
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
    val mass: MassFormatted?,
    val payloadWeights: List<PayloadWeights>?,
    val firstStage: FirstStageResponse?,
    val secondStage: SecondStageResponse?,
    val engines: EngineConfigModel?,
    val landingLegs: LandingLegsModel?,
    val flickr: List<String>?,
    val wikipedia: String?,
    val description: String?,
    val id: String
) {

    constructor(
        response: LaunchResponse.Rocket
    ) : this(
        name = response.configuration?.name,
        type = response.configuration?.name.toRocketType(),
        isActive = null,
        stages = null,
        boosters = null,
        costPerLaunch = null,
        successRate = null,
        firstFlight = null,
        country = null,
        company = null,
        height = null,
        diameter = null,
        mass = null,
        payloadWeights = null,
        firstStage = null,
        secondStage = null,
        engines = null,
        landingLegs = null,
        flickr = null,
        wikipedia = null,
        description = null,
        id = response.id.toString()
    )

    constructor(
        response: RocketResponse
    ) : this(
        name = response.name,
        type = response.name.toRocketType(),
        isActive = response.isActive,
        stages = response.stages,
        boosters = response.boosters,
        costPerLaunch = response.costPerLaunch?.let { DecimalFormat("$#,###.00").format(it) },
        successRate = response.successRate,
        firstFlight = response.firstFlight,
        country = response.country,
        company = response.company,
        height = response.height,
        diameter = response.diameter,
        mass = MassFormatted.formatMass(response.mass?.kg, response.mass?.lb),
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

    companion object {
        private fun String?.toRocketType() = when (this) {
            "Falcon 1" -> RocketType.FALCON_ONE
            "Falcon 9" -> RocketType.FALCON_NINE
            "Falcon Heavy" -> RocketType.FALCON_HEAVY
            "Starship" -> RocketType.STARSHIP
            else -> null
        }
    }
}

data class PayloadWeights(
    val id: String?,
    val name: String?,
    val mass: MassFormatted?
) {

    constructor(
        response: PayloadWeightsResponse
    ) : this(
        id = response.id,
        name = response.name,
        mass = MassFormatted.formatMass(response.kg, response.lb)
    )
}
