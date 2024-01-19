package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.SpecsItem
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

    override val specs: List<SpecsItem>
        get() = listOfNotNull(
            SpecsItem(
                TextResource.string(R.string.second_stage_details_active_label),
                TextResource.string(
                    if (inUse == true) {
                        R.string.second_stage_details_active
                    } else {
                        R.string.second_stage_details_inactive
                    }
                )
            ),
            crewCapacity?.let {
                SpecsItem(
                    TextResource.string(R.string.second_stage_details_crew_capacity_label),
                    TextResource.Companion.string(it.toString())
                )
            },
            maidenFlight?.let {
                SpecsItem(
                    TextResource.string(R.string.second_stage_details_first_flight_label),
                    TextResource.Companion.string(it)
                )
            },
            height?.let {
                SpecsItem(
                    TextResource.string(R.string.second_stage_details_height_label),
                    TextResource.string(R.string.measurement_meters, it.metricFormat())
                )
            },
            diameter?.let {
                SpecsItem(
                    TextResource.string(R.string.second_stage_details_diameter_label),
                    TextResource.string(R.string.measurement_meters, it.metricFormat())
                )
            },
            payloadCapacity?.let {
                SpecsItem(
                    TextResource.string(R.string.second_stage_details_payload_capacity_label),
                    TextResource.string(R.string.mass_kg, it.metricFormat())
                )
            }
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