package uk.co.zac_h.spacex.feature.launch

import androidx.core.net.toUri
import uk.co.zac_h.spacex.core.common.Header
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.core.common.utils.formatCrewDate
import uk.co.zac_h.spacex.core.common.utils.formatDate
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.model.CoreType
import uk.co.zac_h.spacex.core.model.CrewMember
import uk.co.zac_h.spacex.core.model.CrewStatus
import uk.co.zac_h.spacex.core.model.FirstStage
import uk.co.zac_h.spacex.core.model.Launch
import uk.co.zac_h.spacex.core.model.Webcast
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
        launch: Launch
    ) : this(
        id = launch.id,
        upcoming = launch.isUpcoming,
        missionPatch = launch.missionPatchUrl,
        missionName = launch.name,
        rocket = launch.rocket,
        launchDate = launch.net?.toString()?.formatDate(),
        launchDateUnix = launch.net?.toEpochMilli(),
        launchLocation = launch.pad?.name,
        launchLocationMap = launch.pad?.mapImageUrl,
        launchLocationMapUrl = launch.pad?.mapUrl,
        status = Status(launch.status.id, launch.status.name, launch.status.description),
        // 2.3.0 no longer has a hold reason field.
        holdReason = null,
        failReason = launch.failReason,
        description = launch.mission?.description,
        type = launch.mission?.type,
        orbit = launch.mission?.orbit,
        webcast = launch.webcasts
            .groupBy { it.title }.values
            .map { videos -> videos.minBy { it.priority ?: Int.MAX_VALUE } }
            .map { VideoItem(launch.name, launch.imageUrl, it) }
            .ifEmpty { null },
        webcastLive = launch.webcastLive,
        firstStage = launch.firstStages.map { FirstStageItem(it) }.ifEmpty { null },
        crew = launch.crew.map { CrewItem(it) }.ifEmpty { null }
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
        stage: FirstStage
    ) : this(
        id = stage.id.toString(),
        serial = stage.serial.orUnknown(),
        type = stage.type,
        reused = stage.reused,
        totalFlights = stage.totalFlights,
        landingAttempt = stage.landing?.attempt ?: false,
        launcherDetails = stage.launcherDetails,
        landingDetails = stage.landing?.description,
        landingType = stage.landing?.type,
        landingLocation = stage.landing?.location,
        landingLocationFull = stage.landing?.locationName,
        previousFlight = stage.previousFlight,
        turnAroundTimeDays = stage.turnAroundTime?.toDays()?.toInt()?.takeUnless {
            it == 0 && (stage.totalFlights ?: 0) < 2
        }
    )
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
        member: CrewMember
    ) : this(
        id = member.id.toString(),
        name = member.astronaut.name,
        status = member.astronaut.status,
        agency = member.astronaut.agency,
        bio = member.astronaut.bio,
        image = member.astronaut.imageUrl,
        role = member.role,
        firstFlight = member.astronaut.firstFlight?.toString()?.formatCrewDate()
    )
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
        webcast: Webcast
    ) : this(
        title = if (webcast.type != null) {
            "${if (webcast.title.isNullOrEmpty()) missionName else webcast.title} - ${webcast.type}"
        } else {
            if (webcast.title.isNullOrEmpty()) missionName else webcast.title
        },
        description = webcast.description,
        imageUrl = webcast.imageUrl ?: image,
        url = webcast.url,
        source = (webcast.source ?: webcast.url.toUri().host)
            ?.replace("www.", "")
            ?.replace(".com", "")
            ?.replaceFirstChar { it.titlecase(Locale.getDefault()) }
    )
}