package uk.co.zac_h.spacex.feature.launch

import androidx.core.net.toUri
import uk.co.zac_h.spacex.core.common.Header
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
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautFlightDto
import uk.co.zac_h.spacex.network.launchlibrary.model.FirstStageNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchStatusDto
import uk.co.zac_h.spacex.network.launchlibrary.model.VidURLDto
import java.time.Duration
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
        response: LaunchDetailedDto
    ) : this(
        id = response.id.orEmpty(),
        upcoming = response.status.isUpcoming(),
        missionPatch = response.missionPatches?.firstOrNull()?.imageUrl,
        missionName = response.missionName,
        rocket = response.rocket?.configuration?.name,
        launchDate = response.net?.formatDate(),
        launchDateUnix = response.net?.toMillis(),
        launchLocation = response.pad?.name,
        launchLocationMap = response.pad?.mapImage,
        launchLocationMapUrl = response.pad?.mapUrl,
        status = Status(response.status?.id, response.status?.name, response.status?.description),
        // 2.3.0 no longer has a hold reason field.
        holdReason = null,
        failReason = response.failreason,
        description = response.mission?.description,
        type = response.mission?.type,
        orbit = response.mission?.orbit?.name,
        webcast = response.vidUrls?.filter { it.url != null }
            ?.groupBy { it.title }?.values
            ?.map { videos -> videos.minBy { it.priority ?: Int.MAX_VALUE } }
            ?.map { VideoItem(response.missionName, response.image?.imageUrl, it) },
        webcastLive = response.webcastLive ?: false,
        firstStage = response.rocket?.launcherStage?.map { FirstStageItem(it) },
        crew = response.rocket?.spacecraftStage?.flatMap { it.launchCrew.orEmpty() }
            ?.map { CrewItem(it) }
    )

    data class Status(
        val type: Int?,
        val name: String?,
        val description: String?
    )

    val cores: List<RecyclerViewItem>
        get() = firstStage?.let { firstStageList ->
            if (firstStageList.size > 1) {
                firstStageList.groupBy { firstStageItem ->
                    firstStageItem.type
                }.flatMap { firstStageItem ->
                    listOf(Header(firstStageItem.key.type)) + firstStageItem.value
                }
            } else {
                firstStageList
            }
        } ?: emptyList()

    companion object {

        private fun LaunchStatusDto?.isUpcoming() = this?.id in listOf(1, 2, 5, 6, 8)

        private val LaunchDetailedDto.missionName: String
            get() = mission?.name ?: name.orEmpty()
    }
}

data class FirstStageItem(
    val id: String,
    val serial: String,
    val type: CoreType,
    val reused: Boolean,
    val totalFlights: Int?,
    val landingAttempt: Boolean,
    val launcherDetails: String?,
    val landingDetails: String?,
    val landingType: String?,
    val landingLocation: String?,
    val landingLocationFull: String?,
    val previousFlight: String?,
    val turnAroundTimeDays: Int?
) : RecyclerViewItem {

    constructor(
        response: FirstStageNormalDto
    ) : this(
        id = response.id.toString(),
        serial = response.launcher?.serialNumber.orUnknown(),
        type = response.type.toCoreType(),
        reused = response.reused ?: false,
        totalFlights = response.launcher?.flights,
        landingAttempt = response.landing?.attempt ?: false,
        launcherDetails = response.launcher?.details,
        landingDetails = response.landing?.description,
        landingType = response.landing?.type?.abbrev,
        landingLocation = response.landing?.landingLocation?.abbrev,
        landingLocationFull = response.landing?.landingLocation?.name,
        previousFlight = response.previousFlight?.name,
        turnAroundTimeDays = response.turnAroundTime?.isoDurationDays()?.takeUnless {
            it == 0 && (response.launcher?.flights ?: 0) < 2
        }
    )

    companion object {

        private fun String?.toCoreType() = when (this) {
            "Core" -> CoreType.CORE
            "Strap-On Booster" -> CoreType.BOOSTER
            else -> CoreType.OTHER
        }

        /** 2.3.0 sends turnaround as an ISO 8601 duration, e.g. "P32DT21H13M9S". */
        private fun String.isoDurationDays(): Int? =
            runCatching { Duration.parse(this).toDays().toInt() }.getOrNull()
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
        response: AstronautFlightDto
    ) : this(
        id = response.id.toString(),
        name = response.astronaut?.name,
        status = response.astronaut?.status?.name.toCrewStatus(),
        agency = response.astronaut?.agency?.name,
        bio = response.astronaut?.bio,
        image = response.astronaut?.image?.imageUrl,
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
        response: VidURLDto
    ) : this(
        title = if (response.type != null) {
            "${if (response.title.isNullOrEmpty()) missionName else response.title} - ${response.type?.name}"
        } else {
            if (response.title.isNullOrEmpty()) missionName else response.title
        },
        description = response.description,
        imageUrl = response.featureImage ?: image,
        url = response.url.orEmpty(),
        source = (response.source ?: response.url?.toUri()?.host)
            ?.replace("www.", "")
            ?.replace(".com", "")
            ?.replaceFirstChar { it.titlecase(Locale.getDefault()) }
    )
}