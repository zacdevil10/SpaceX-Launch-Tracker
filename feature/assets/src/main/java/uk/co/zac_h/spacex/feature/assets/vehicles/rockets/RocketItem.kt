package uk.co.zac_h.spacex.feature.assets.vehicles.rockets

import uk.co.zac_h.spacex.core.common.types.RocketFamily
import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class RocketItem(
    override val id: Int,
    override val title: String?,
    val fullName: String?,
    val type: RocketType?,
    val family: RocketFamily,
    override val description: String?,
    override val longDescription: String?,
    val stages: Int?,
    val length: Float?,
    val diameter: Float?,
    val maidenFlight: String?,
    val maidenFlightMillis: Long?,
    val launchMass: Int?,
    val leoCapacity: Int?,
    val gtoCapacity: Int?,
    val toThrust: Int?,
    override val imageUrl: String?,
    val consecutiveSuccessfulLaunches: Int?,
    val successfulLaunches: Int?,
    val failedLaunches: Int?,
    val pendingLaunches: Int?
) : VehicleItem {

    constructor(response: AgencyResponse.Launcher) : this(
        id = response.id,
        title = response.fullName,
        fullName = response.fullName,
        type = response.name?.toType(),
        family = response.family.toFamily(),
        description = response.description,
        longDescription = response.description,
        stages = response.maxStage,
        length = response.length,
        diameter = response.diameter,
        maidenFlight = response.maidenFlight,
        maidenFlightMillis = response.maidenFlight?.toMillis(),
        launchMass = response.launchMass,
        leoCapacity = response.leoCapacity,
        gtoCapacity = response.gtoCapacity,
        toThrust = response.toThrust,
        imageUrl = response.imageUrl,
        consecutiveSuccessfulLaunches = response.consecutiveSuccessfulLaunches,
        successfulLaunches = response.successfulLaunches,
        failedLaunches = response.failedLaunches,
        pendingLaunches = response.pendingLaunches
    )

    val successRate: Float?
        get() = if (successfulLaunches != null && failedLaunches != null && successfulLaunches > 0) {
            successfulLaunches.toFloat()
                .div(successfulLaunches.toFloat().plus(failedLaunches.toFloat())) * 100
        } else {
            null
        }

    companion object {

        fun String.toMillis(): Long? = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.ENGLISH
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this)?.time

        fun String?.toFamily(): RocketFamily = when (this) {
            "Falcon" -> RocketFamily.FALCON
            "Starship" -> RocketFamily.STARSHIP
            else -> RocketFamily.NONE
        }

        fun String.toType(): RocketType? = when (this) {
            "Falcon 1" -> RocketType.FALCON_ONE
            "Falcon 9" -> RocketType.FALCON_NINE
            "Falcon Heavy" -> RocketType.FALCON_HEAVY
            "Starship" -> RocketType.STARSHIP
            else -> null
        }
    }
}
