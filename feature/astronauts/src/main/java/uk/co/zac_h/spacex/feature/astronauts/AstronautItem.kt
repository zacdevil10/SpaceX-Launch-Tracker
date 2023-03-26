package uk.co.zac_h.spacex.feature.astronauts

import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.common.utils.formatCrewDate
import uk.co.zac_h.spacex.network.SPACEX_CREW_LOST_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_ACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_DECEASED
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_INACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_RETIRED
import uk.co.zac_h.spacex.network.dto.spacex.AstronautResponse
import uk.co.zac_h.spacex.network.dto.spacex.CrewStatus

data class AstronautItem(
    val id: Int,
    val name: String?,
    val status: CrewStatus,
    val agency: String?,
    val nationality: String?,
    val bio: String?,
    val image: String?,
    val firstFlight: String?
) : RecyclerViewItem {

    constructor(
        response: AstronautResponse
    ) : this(
        id = response.id,
        name = response.name,
        status = response.status?.name.toCrewStatus(),
        agency = response.agency?.name,
        nationality = response.nationality,
        bio = response.bio,
        image = response.image,
        firstFlight = response.firstFlight?.formatCrewDate()
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