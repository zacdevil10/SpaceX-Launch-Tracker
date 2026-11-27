package uk.co.zac_h.spacex.core.data.mapper

import uk.co.zac_h.spacex.core.model.Astronaut
import uk.co.zac_h.spacex.core.model.CrewStatus
import uk.co.zac_h.spacex.core.model.Launcher
import uk.co.zac_h.spacex.core.model.Rocket
import uk.co.zac_h.spacex.core.model.RocketFamily
import uk.co.zac_h.spacex.core.model.RocketType
import uk.co.zac_h.spacex.core.model.Spacecraft
import uk.co.zac_h.spacex.core.model.SpacecraftConfiguration
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherConfigDetailedSerializerNoManufacturerDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftConfigDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto

internal fun AstronautEndpointNormalDto.toAstronaut() = Astronaut(
    id = id,
    name = name,
    status = CrewStatus.fromApiName(status?.name),
    agency = agency?.name,
    nationalities = nationality.orEmpty().mapNotNull { it.nationalityName },
    bio = bio,
    imageUrl = image?.imageUrl,
    firstFlight = firstFlight.toInstantOrNull()
)

internal fun LauncherConfigDetailedSerializerNoManufacturerDto.toRocket() = Rocket(
    id = id,
    name = name,
    fullName = fullName,
    type = name.toRocketType(),
    // 2.3.0 lists every family a config belongs to, e.g. "Falcon" and "Falcon 9".
    family = families.orEmpty()
        .map { it.name.toRocketFamily() }
        .firstOrNull { it != RocketFamily.NONE } ?: RocketFamily.NONE,
    description = description,
    imageUrl = image?.imageUrl,
    maxStages = maxStage,
    length = length,
    diameter = diameter,
    maidenFlight = maidenFlight.toLocalDateOrNull(),
    launchMass = launchMass,
    leoCapacity = leoCapacity,
    gtoCapacity = gtoCapacity,
    takeoffThrust = toThrust,
    consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
    successfulLaunches = successfulLaunches,
    failedLaunches = failedLaunches,
    pendingLaunches = pendingLaunches
)

internal fun LauncherDetailedDto.toLauncher() = Launcher(
    id = id,
    serial = serialNumber,
    status = status?.name,
    details = details,
    configuration = launcherConfig?.fullName,
    imageUrl = image?.imageUrl,
    flights = flights,
    firstLaunch = firstLaunchDate.toInstantOrNull(),
    lastLaunch = lastLaunchDate.toInstantOrNull()
)

internal fun SpacecraftNormalDto.toSpacecraft() = Spacecraft(
    id = id,
    name = name,
    serial = serialNumber,
    status = status?.name,
    description = description,
    configuration = spacecraftConfig?.name,
    imageUrl = image?.imageUrl
)

internal fun SpacecraftConfigDetailedDto.toSpacecraftConfiguration() = SpacecraftConfiguration(
    id = id,
    name = name,
    inUse = inUse,
    capability = capability,
    history = history,
    details = details,
    maidenFlight = maidenFlight.toLocalDateOrNull(),
    height = height,
    diameter = diameter,
    humanRated = humanRated,
    crewCapacity = crewCapacity,
    payloadCapacity = payloadCapacity,
    flightLife = flightLife,
    imageUrl = image?.imageUrl,
    wikiUrl = wikiLink,
    infoUrl = infoLink
)

private fun String?.toRocketFamily(): RocketFamily = when (this) {
    "Falcon" -> RocketFamily.FALCON
    "Starship" -> RocketFamily.STARSHIP
    else -> RocketFamily.NONE
}

private fun String?.toRocketType(): RocketType? = when (this) {
    "Falcon 1" -> RocketType.FALCON_ONE
    "Falcon 9" -> RocketType.FALCON_NINE
    "Falcon Heavy" -> RocketType.FALCON_HEAVY
    "Starship" -> RocketType.STARSHIP
    else -> null
}
