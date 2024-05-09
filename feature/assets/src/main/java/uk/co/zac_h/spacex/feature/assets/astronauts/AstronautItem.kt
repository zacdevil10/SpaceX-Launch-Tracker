package uk.co.zac_h.spacex.feature.assets.astronauts

import uk.co.zac_h.spacex.core.common.types.CrewStatus
import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.core.common.utils.formatCrewDate
import uk.co.zac_h.spacex.feature.assets.vehicles.SpecsItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.network.SPACEX_CREW_LOST_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_ACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_DECEASED
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_INACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_RETIRED
import uk.co.zac_h.spacex.network.dto.spacex.AstronautResponse

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

    constructor(
        response: AstronautResponse
    ) : this(
        id = response.id,
        title = response.name,
        status = response.status?.name.toCrewStatus(),
        agency = response.agency?.name,
        nationality = response.nationality,
        description = response.bio,
        longDescription = response.bio,
        imageUrl = response.image,
        firstFlight = response.firstFlight?.formatCrewDate()
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

    companion object {
        private fun String?.toCrewStatus() = when (this) {
            SPACEX_CREW_STATUS_IN_TRAINING -> CrewStatus.IN_TRAINING
            SPACEX_CREW_STATUS_ACTIVE -> CrewStatus.ACTIVE
            SPACEX_CREW_STATUS_INACTIVE -> CrewStatus.INACTIVE
            SPACEX_CREW_STATUS_RETIRED -> CrewStatus.RETIRED
            SPACEX_CREW_STATUS_DECEASED -> CrewStatus.DECEASED
            SPACEX_CREW_LOST_IN_TRAINING -> CrewStatus.LOST_IN_TRAINING
            else -> CrewStatus.UNKNOWN
        }
    }
}