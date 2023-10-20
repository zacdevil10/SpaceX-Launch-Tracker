package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class SecondStageItem(
    override val id: String,
    override val title: String?,
    val name: String?,
    override val description: String?,
    override val longDescription: String?,
    val inUse: Boolean?,
    val capability: String?,
    val history: String?,
    val details: String?,
    val maidenFlight: String?,
    val maidenFlightMillis: Long?,
    val height: Float?,
    val diameter: Float?,
    val humanRated: Boolean?,
    val crewCapacity: Int?,
    val payloadCapacity: Int?,
    val flightLife: String?,
    override val imageUrl: String?,
    val wiki: String?,
    val info: String?
) : VehicleItem {

    constructor(response: AgencyResponse.Spacecraft) : this(
        id = response.id,
        title = response.name,
        name = response.name,
        description = response.history,
        longDescription = "${response.history}\n\n${response.details}",
        inUse = response.inUse,
        capability = response.capability,
        history = response.history,
        details = response.details,
        maidenFlight = response.maidenFlight,
        maidenFlightMillis = response.maidenFlight?.toMillis(),
        height = response.height,
        diameter = response.diameter,
        humanRated = response.humanRated,
        crewCapacity = response.crewCapacity,
        payloadCapacity = response.payloadCapacity,
        flightLife = response.flightLife,
        imageUrl = response.imageUrl,
        wiki = response.wikiLink,
        info = response.infoLink
    )

    companion object {

        fun String.toMillis(): Long? = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.ENGLISH
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this)?.time
    }
}