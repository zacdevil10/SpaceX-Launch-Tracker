package uk.co.zac_h.spacex.feature.assets.astronauts

import uk.co.zac_h.spacex.core.model.Astronaut
import uk.co.zac_h.spacex.core.model.CrewStatus
import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.feature.assets.vehicles.SpecsItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.formatLocal

data class AstronautItem(
    override val id: Int,
    override val title: String?,
    val status: CrewStatus,
    val agency: String?,
    val nationality: String?,
    override val description: String?,
    override val longDescription: String?,
    override val imageUrl: String?,
    val firstFlight: String?
) : VehicleItem {

    constructor(astronaut: Astronaut) : this(
        id = astronaut.id,
        title = astronaut.name,
        status = astronaut.status,
        agency = astronaut.agency,
        nationality = astronaut.nationalities.joinToString().ifEmpty { null },
        description = astronaut.bio,
        longDescription = astronaut.bio,
        imageUrl = astronaut.imageUrl,
        firstFlight = astronaut.firstFlight?.formatLocal("dd MMM yy")
    )

    override val specs: List<SpecsItem>
        get() = listOfNotNull(
            SpecsItem(
                TextResource.string("Status"),
                TextResource.string(status.status)
            ),
            firstFlight?.let {
                SpecsItem(
                    TextResource.string("First flight"),
                    TextResource.string(it)
                )
            }
        )
}
