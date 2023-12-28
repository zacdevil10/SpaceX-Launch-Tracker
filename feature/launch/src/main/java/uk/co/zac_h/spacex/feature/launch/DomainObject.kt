package uk.co.zac_h.spacex.feature.launch

import androidx.core.net.toUri
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.common.types.CoreType
import uk.co.zac_h.spacex.core.common.types.CrewStatus
import uk.co.zac_h.spacex.core.common.utils.formatCrewDate
import uk.co.zac_h.spacex.core.common.utils.formatDate
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.utils.toMillis
import uk.co.zac_h.spacex.network.SPACEX_CREW_LOST_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_ACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_DECEASED
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_INACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_RETIRED
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import java.util.Locale

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
    val webcast: List<VideoItem>?,
    val webcastLive: Boolean,
    val firstStage: List<FirstStageItem>?,
    val crew: List<CrewItem>?
) : RecyclerViewItem {

    constructor(
        response: LaunchResponse
    ) : this(
        id = response.id,
        upcoming = response.status.isUpcoming(),
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
        webcast = response.video?.groupBy { it.title }?.values
            ?.map { videos -> videos.minBy { it.priority } }
            ?.map { VideoItem(response.mission?.name ?: response.name, response.image, it) },
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

    companion object {

        private fun LaunchResponse.Status?.isUpcoming() = this?.id in listOf(1, 2, 5, 6, 8)
    }
}

data class FirstStageItem(
    val id: String,
    val serial: String,
    val type: CoreType,
    val reused: Boolean,
    val totalFlights: Int?,
    val landingAttempt: Boolean,
    val landingDescription: String?,
    val landingType: String?,
    val landingLocation: String?,
    val landingLocationFull: String?,
    val previousFlight: String?,
    val turnAroundTimeDays: Int?
) : RecyclerViewItem {

    constructor(
        response: LaunchResponse.Rocket.LauncherStage
    ) : this(
        id = response.id.toString(),
        serial = response.launcher?.serialNumber.orUnknown(),
        type = response.type.toCoreType(),
        reused = response.reused ?: false,
        totalFlights = response.launcher?.flights,
        landingAttempt = response.landing?.attempt ?: false,
        landingDescription = response.landing?.description,
        landingType = response.landing?.type?.abbrev,
        landingLocation = response.landing?.location?.abbrev,
        landingLocationFull = response.landing?.location?.name,
        previousFlight = response.previousFlight?.name,
        turnAroundTimeDays = if (response.turnAroundTimeDays == 0
            && (response.launcher?.flights ?: 0) < 2
        ) {
            null
        } else {
            response.turnAroundTimeDays
        }
    )

    companion object {

        private fun String?.toCoreType() = when (this) {
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
        private fun String?.toCrewStatus() = when (this) {
            SPACEX_CREW_STATUS_ACTIVE -> CrewStatus.ACTIVE
            SPACEX_CREW_STATUS_INACTIVE -> CrewStatus.INACTIVE
            SPACEX_CREW_STATUS_RETIRED -> CrewStatus.RETIRED
            SPACEX_CREW_STATUS_DECEASED -> CrewStatus.DECEASED
            SPACEX_CREW_LOST_IN_TRAINING -> CrewStatus.LOST_IN_TRAINING
            else -> CrewStatus.UNKNOWN
        }
    }
}

data class VideoItem(
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val url: String,
    val source: String?,
) : RecyclerViewItem {

    constructor(
        missionName: String,
        image: String?,
        response: LaunchResponse.Video
    ) : this(
        title = if (response.type != null) {
            "${if (response.title.isNullOrEmpty()) missionName else response.title} - ${response.type?.name}"
        } else {
            if (response.title.isNullOrEmpty()) missionName else response.title
        },
        description = response.description,
        imageUrl = response.featureImage ?: image,
        url = response.url,
        source = (response.source ?: response.url.toUri().host)
            ?.replace("www.", "")
            ?.replace(".com", "")
            ?.replaceFirstChar { it.titlecase(Locale.getDefault()) }
    )
}