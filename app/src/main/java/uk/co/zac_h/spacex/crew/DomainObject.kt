package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.core.utils.orUnknown
import uk.co.zac_h.spacex.network.*
import uk.co.zac_h.spacex.network.dto.spacex.CrewQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.CrewStatus

data class Crew(
    val name: String?,
    val status: CrewStatus,
    val agency: String?,
    val bio: String? = null,
    val image: String?,
    val wikipedia: String?,
    val role: String?,
    val firstFlight: String? = null,
    val id: String
) {

    constructor(
        response: CrewQueriedResponse,
        role: String? = null
    ) : this(
        name = response.name,
        status = response.status.toCrewStatus(),
        agency = response.agency.orUnknown(),
        image = response.image,
        wikipedia = response.wikipedia,
        role = role,
        id = response.id
    )

    companion object {
        fun String?.toCrewStatus() = when (this) {
            SPACEX_CREW_STATUS_ACTIVE -> CrewStatus.ACTIVE
            SPACEX_CREW_STATUS_INACTIVE -> CrewStatus.INACTIVE
            SPACEX_CREW_STATUS_RETIRED -> CrewStatus.RETIRED
            SPACEX_CREW_STATUS_DECEASED -> CrewStatus.DECEASED
            SPACEX_CREW_LOST_IN_TRAINING -> CrewStatus.LOST_IN_TRAINING
            else -> CrewStatus.UNKNOWN
        }
    }
}
