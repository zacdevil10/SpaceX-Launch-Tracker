package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.core.utils.orUnknown
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_ACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_INACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_RETIRED
import uk.co.zac_h.spacex.network.dto.spacex.*

data class Crew(
    val name: String?,
    val status: CrewStatus?,
    val agency: String?,
    val image: String?,
    val wikipedia: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    val role: String?,
    val id: String
) {

    constructor(
        response: LaunchResponse.Rocket.SpacecraftStage.LaunchCrew
    ) : this(
        name = response.astronaut?.name,
        status = response.astronaut?.status?.name?.toCrewStatus(),
        agency = response.astronaut?.agency?.name,
        image = response.astronaut?.profileImage,
        wikipedia = null,
        role = response.role?.role,
        id = response.id.toString()
    )

    constructor(
        response: CrewResponse
    ) : this(
        name = response.name,
        status = response.status.toCrewStatus(),
        agency = response.agency.orUnknown(),
        image = response.image,
        wikipedia = response.wikipedia,
        launchIds = response.launches,
        role = null,
        id = response.id
    )

    constructor(
        response: CrewQueriedResponse,
        role: String? = null
    ) : this(
        name = response.name,
        status = response.status.toCrewStatus(),
        agency = response.agency.orUnknown(),
        image = response.image,
        wikipedia = response.wikipedia,
        launches = response.launches?.map { Launch(it) },
        role = role,
        id = response.id
    )

    constructor(
        response: LaunchCrewResponse
    ) : this(
        name = null,
        status = null,
        agency = null,
        image = null,
        wikipedia = null,
        role = response.role,
        id = response.crew
    )

    constructor(
        response: LaunchCrewQueriedResponse
    ) : this(
        name = response.crew.name,
        status = response.crew.status.toCrewStatus(),
        agency = response.crew.agency,
        image = response.crew.image,
        wikipedia = response.crew.wikipedia,
        launches = response.crew.launches?.map { Launch(it) },
        role = response.role,
        id = response.crew.id
    )

    companion object {
        private fun String?.toCrewStatus() = when (this) {
            SPACEX_CREW_STATUS_ACTIVE -> CrewStatus.ACTIVE
            SPACEX_CREW_STATUS_INACTIVE -> CrewStatus.INACTIVE
            SPACEX_CREW_STATUS_RETIRED -> CrewStatus.RETIRED
            else -> CrewStatus.UNKNOWN
        }
    }
}
