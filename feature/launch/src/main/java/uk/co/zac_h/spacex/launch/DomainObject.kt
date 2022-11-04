package uk.co.zac_h.spacex.launch

import android.content.res.Resources
import uk.co.zac_h.spacex.core.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.types.CoreType
import uk.co.zac_h.spacex.core.utils.*
import uk.co.zac_h.spacex.network.*
import uk.co.zac_h.spacex.network.dto.spacex.CrewStatus
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse

data class LaunchItem(
    val id: String,
    val upcoming: Boolean,
    val missionPatch: String?,
    val missionName: String,
    val rocket: String?,
    val launchDate: String?,
    val launchDateUnix: Long?,
    val launchLocation: String?,
    val launchLocationMap: String?,
    val launchLocationMapUrl: String?,
    val status: Status,
    val holdReason: String?,
    val failReason: String?,
    val description: String?,
    val type: String?,
    val orbit: String?,
    val webcast: String?,
    val webcastLive: Boolean,
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
        launchLocationMap = response.pad?.mapImage,
        launchLocationMapUrl = response.pad?.mapUrl,
        status = Status(response.status?.id, response.status?.name, response.status?.description),
        holdReason = response.holdReason,
        failReason = response.failReason,
        description = response.mission?.description,
        type = response.mission?.type,
        orbit = response.mission?.orbit?.name,
        webcast = response.video?.firstOrNull()?.url,
        webcastLive = response.webcastLive ?: false,
        firstStage = response.rocket?.launcherStage?.mapNotNull { launcherStage ->
            launcherStage?.let { FirstStageItem(it) }
        },
        crew = response.rocket?.spacecraftStage?.launchCrew?.mapNotNull { it?.let { CrewItem(it) } }
    )

    data class Status(
        val type: Int?,
        val name: String?,
        val description: String?
    )

    fun countdown(resources: Resources): String? {
        val remaining = calculateCountdown()

        return when {
            !upcoming -> null
            remaining < 0 -> resources.getString(R.string.launches_webcast_live)
            else -> String.format(
                "T-%02d:%02d:%02d:%02d",
                remaining.toCountdownDays(),
                remaining.toCountdownHours(),
                remaining.toCountdownMinutes(),
                remaining.toCountdownSeconds()
            )
        }
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
