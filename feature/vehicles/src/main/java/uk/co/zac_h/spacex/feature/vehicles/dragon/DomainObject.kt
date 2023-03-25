package uk.co.zac_h.spacex.feature.vehicles.dragon

import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class SpacecraftItem(
    val id: String,
    val name: String?,
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
    val imageUrl: String?,
    val wiki: String?,
    val info: String?
) {

    constructor(response: AgencyResponse.Spacecraft) : this(
        id = response.id,
        name = response.name,
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