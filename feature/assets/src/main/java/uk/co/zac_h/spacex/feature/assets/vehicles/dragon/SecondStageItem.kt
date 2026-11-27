package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.core.common.utils.metricFormat
import uk.co.zac_h.spacex.core.model.SpacecraftConfiguration
import uk.co.zac_h.spacex.feature.assets.R
import uk.co.zac_h.spacex.feature.assets.vehicles.SpecsItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.toEpochMilliUtc

data class SecondStageItem(
    override val id: Int,
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

    constructor(configuration: SpacecraftConfiguration) : this(
        id = configuration.id,
        title = configuration.name,
        name = configuration.name,
        description = configuration.history,
        longDescription = "${configuration.history}\n\n${configuration.details}",
        inUse = configuration.inUse,
        capability = configuration.capability,
        history = configuration.history,
        details = configuration.details,
        maidenFlight = configuration.maidenFlight?.toString(),
        maidenFlightMillis = configuration.maidenFlight?.toEpochMilliUtc(),
        height = configuration.height?.toFloat(),
        diameter = configuration.diameter?.toFloat(),
        humanRated = configuration.humanRated,
        crewCapacity = configuration.crewCapacity,
        payloadCapacity = configuration.payloadCapacity,
        flightLife = configuration.flightLife,
        imageUrl = configuration.imageUrl,
        wiki = configuration.wikiUrl,
        info = configuration.infoUrl
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
}
