package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.core.types.CoreType
import uk.co.zac_h.spacex.core.utils.*
import uk.co.zac_h.spacex.crew.Crew
import uk.co.zac_h.spacex.crew.Crew.Companion.toCrewStatus
import uk.co.zac_h.spacex.launches.adapters.RecyclerViewItem
import uk.co.zac_h.spacex.network.dto.spacex.CrewStatus
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import uk.co.zac_h.spacex.network.dto.spacex.Mass
import uk.co.zac_h.spacex.network.dto.spacex.MassFormatted
import uk.co.zac_h.spacex.statistics.graphs.padstats.Launchpad
import uk.co.zac_h.spacex.vehicles.rockets.Rocket

data class LaunchItem(
    val id: String,
    val upcoming: Boolean,
    val missionPatch: String?,
    val missionName: String,
    val rocket: String?,
    val launchDate: String?,
    val launchDateUnix: Long?,
    val launchLocation: String?,
    val description: String?,
    val webcast: String?,
    val firstStage: List<FirstStageItem>?,
    val crew: List<CrewItem>?
) : RecyclerViewItem {

    constructor(
        response: LaunchResponse
    ) : this(
        id = response.id,
        upcoming = response.status?.id != 3,
        missionPatch = response.missionPatches?.firstOrNull()?.imageUrl,
        missionName = response.mission?.name ?: response.name,
        rocket = response.rocket?.configuration?.name,
        launchDate = response.net?.formatDate(),
        launchDateUnix = response.net?.toMillis(),
        launchLocation = response.pad?.name,
        description = response.mission?.description,
        webcast = response.video?.firstOrNull()?.url,
        firstStage = response.rocket?.launcherStage?.mapNotNull { launcherStage ->
            launcherStage?.let { FirstStageItem(it) }
        },
        crew = response.rocket?.spacecraftStage?.launchCrew?.mapNotNull { it?.let { CrewItem(it) } }
    )

    fun countdown(): String? {
        val remaining = calculateCountdown()

        if (remaining < 0) return null

        return String.format(
            "T-%02d:%02d:%02d:%02d",
            remaining.toCountdownDays(),
            remaining.toCountdownHours(),
            remaining.toCountdownMinutes(),
            remaining.toCountdownSeconds()
        )
    }

    private fun calculateCountdown() = launchDateUnix?.minus(System.currentTimeMillis()) ?: 0
}

data class FirstStageItem(
    val id: String,
    val serial: String?,
    val type: CoreType,
    val reused: Boolean?,
    val landingAttempt: Boolean?,
    val landingSuccess: Boolean?,
    val landingType: String?,
    val landingLocation: String?
) : RecyclerViewItem {

    constructor(
        response: LaunchResponse.Rocket.LauncherStage
    ) : this(
        id = response.id.toString(),
        serial = response.launcher?.serialNumber,
        type = response.type.toCoreType(),
        reused = response.reused,
        landingAttempt = response.landing?.attempt,
        landingSuccess = response.landing?.success,
        landingType = response.landing?.type?.abbrev,
        landingLocation = response.landing?.location?.abbrev
    )

    companion object {

        fun String?.toCoreType() = when (this) {
            "Core" -> CoreType.CORE
            "Strap-On Booster" -> CoreType.BOOSTER
            else -> CoreType.OTHER
        }
    }
}

data class CrewItem(
    val id: String,
    val name: String?,
    val status: CrewStatus,
    val agency: String?,
    val bio: String?,
    val image: String?,
    val role: String?,
    val firstFlight: String?,
) : RecyclerViewItem {

    constructor(
        response: LaunchResponse.Rocket.SpacecraftStage.LaunchCrew
    ) : this(
        id = response.id.toString(),
        name = response.astronaut?.name,
        status = response.astronaut?.status?.name.toCrewStatus(),
        agency = response.astronaut?.agency?.name,
        bio = response.astronaut?.bio,
        image = response.astronaut?.profileImage,
        role = response.role?.role,
        firstFlight = response.astronaut?.firstFlight?.formatCrewDate()
    )
}

//Replace
data class Launch(
    val tbd: Boolean?,
    val net: Boolean?,
    val rocketId: String? = null,
    val rocket: Rocket? = null,
    val success: Boolean?,
    val details: String?,
    val crew: List<Crew>?,
    val launchpadId: String? = null,
    val launchpad: Launchpad? = null,
) {

    constructor(
        response: LaunchResponse
    ) : this(
        tbd = response.status?.id == 2,
        net = response.net != null,
        rocket = response.rocket?.let { Rocket(it) },
        success = response.status?.id == 3,
        details = response.mission?.description,
        crew = emptyList(),
        launchpadId = response.pad?.id.toString(),
    )
}

data class Payload(
    val name: String?,
    val type: String?,
    val reused: Boolean?,
    val launchId: String? = null,
    val launch: Launch? = null,
    val customers: List<String>?,
    val noradIds: List<Int>?,
    val nationalities: List<String>?,
    val manufacturers: List<String>?,
    val mass: Mass?,
    val formattedMass: MassFormatted?,
    val orbit: String?,
    val referenceSystem: String?,
    val regime: String?,
    val longitude: Float?,
    val semiMajorAxisKm: Float?,
    val eccentricity: Float?,
    val periapsisKm: Float?,
    val apoapsisKm: Float?,
    val inclination: Float?,
    val period: Float?,
    val lifespan: Float?,
    val epoch: String?,
    val meanMotion: Float?,
    val raan: Float?,
    val argOfPericenter: Float?,
    val meanAnomaly: Float?,
    val dragon: PayloadDragon?,
    val id: String
)

data class PayloadDragon(
    val capsule: String?,
    val massReturned: MassFormatted?,
    val flightTime: Int?,
    val manifest: String?,
    val waterLanding: Boolean?,
    val landLanding: Boolean?
)
