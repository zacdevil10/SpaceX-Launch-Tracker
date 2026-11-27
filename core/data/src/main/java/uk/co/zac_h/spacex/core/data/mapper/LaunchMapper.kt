package uk.co.zac_h.spacex.core.data.mapper

import uk.co.zac_h.spacex.core.model.Astronaut
import uk.co.zac_h.spacex.core.model.CoreType
import uk.co.zac_h.spacex.core.model.CrewMember
import uk.co.zac_h.spacex.core.model.CrewStatus
import uk.co.zac_h.spacex.core.model.FirstStage
import uk.co.zac_h.spacex.core.model.Landing
import uk.co.zac_h.spacex.core.model.Launch
import uk.co.zac_h.spacex.core.model.LaunchStatus
import uk.co.zac_h.spacex.core.model.Mission
import uk.co.zac_h.spacex.core.model.Pad
import uk.co.zac_h.spacex.core.model.Webcast
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautFlightDto
import uk.co.zac_h.spacex.network.launchlibrary.model.FirstStageNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.VidURLDto
import java.time.Duration

internal fun LaunchDetailedDto.toLaunch() = Launch(
    id = id,
    name = mission?.name ?: name.orEmpty(),
    net = net.toInstantOrNull(),
    status = LaunchStatus(
        id = status?.id,
        name = status?.name,
        abbreviation = status?.abbrev,
        description = status?.description
    ),
    rocket = rocket?.configuration?.name,
    missionPatchUrl = missionPatches?.firstOrNull()?.imageUrl,
    imageUrl = image?.imageUrl,
    pad = pad?.let { Pad(name = it.name, mapImageUrl = it.mapImage, mapUrl = it.mapUrl) },
    mission = mission?.let {
        Mission(name = it.name, description = it.description, type = it.type, orbit = it.orbit?.name)
    },
    failReason = failreason,
    webcasts = vidUrls.orEmpty().mapNotNull { it.toWebcast() },
    webcastLive = webcastLive ?: false,
    firstStages = rocket?.launcherStage.orEmpty().map { it.toFirstStage() },
    crew = rocket?.spacecraftStage.orEmpty().flatMap { it.launchCrew.orEmpty() }.map { it.toCrewMember() },
    lastUpdated = lastUpdated.toInstantOrNull()
)

private fun VidURLDto.toWebcast(): Webcast? = url?.let {
    Webcast(
        url = it,
        title = title,
        type = type?.name,
        description = description,
        imageUrl = featureImage,
        source = source,
        priority = priority
    )
}

private fun FirstStageNormalDto.toFirstStage() = FirstStage(
    id = id,
    serial = launcher?.serialNumber,
    type = CoreType.fromApiName(type),
    reused = reused ?: false,
    totalFlights = launcher?.flights,
    launcherDetails = launcher?.details,
    landing = landing?.let {
        Landing(
            attempt = it.attempt ?: false,
            description = it.description,
            type = it.type?.abbrev,
            location = it.landingLocation?.abbrev,
            locationName = it.landingLocation?.name
        )
    },
    previousFlight = previousFlight?.name,
    // Launch Library sends turnaround as an ISO 8601 duration, such as "P32DT21H13M9S".
    turnAroundTime = turnAroundTime?.let { runCatching { Duration.parse(it) }.getOrNull() }
)

private fun AstronautFlightDto.toCrewMember() = CrewMember(
    id = id,
    role = role?.role,
    astronaut = Astronaut(
        id = astronaut?.id ?: id,
        name = astronaut?.name,
        status = CrewStatus.fromApiName(astronaut?.status?.name),
        agency = astronaut?.agency?.name,
        nationalities = astronaut?.nationality.orEmpty().mapNotNull { it.nationalityName },
        bio = astronaut?.bio,
        imageUrl = astronaut?.image?.imageUrl,
        firstFlight = astronaut?.firstFlight.toInstantOrNull()
    )
)
