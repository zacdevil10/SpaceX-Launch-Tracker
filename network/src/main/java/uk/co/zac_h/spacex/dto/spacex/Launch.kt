package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

data class LaunchResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER) val flightNumber: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_NAME) val missionName: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_UTC) val launchDateUtc: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_UNIX) val launchDateUnix: Long?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_LOCAL) val launchDateLocal: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_PRECISION) val datePrecision: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UTC) val staticFireDateUtc: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UNIX) val staticFireDateUnix: Long?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_TBD) val tbd: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_NET) val net: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WINDOW) val window: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_ROCKET) val rocket: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_SUCCESS) val success: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES) val failures: List<LaunchFailures>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_UPCOMING) val upcoming: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DETAILS) val details: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS) val fairings: Fairings?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW) val crew: List<LaunchCrewResponse>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_SHIPS) val ships: List<String>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CAPSULES) val capsules: List<String>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_PAYLOADS) val payloads: List<String>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_LAUNCHPAD) val launchpad: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES) val cores: List<LaunchCoreResponse>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_LINKS) val links: LaunchLinks?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_AUTO_UPDATE) val autoUpdate: Boolean?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class LaunchQueriedResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER) val flightNumber: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_NAME) val missionName: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_UTC) val launchDateUtc: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_UNIX) val launchDateUnix: Long?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_LOCAL) val launchDateLocal: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_PRECISION) val datePrecision: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UTC) val staticFireDateUtc: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UNIX) val staticFireDateUnix: Long?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_TBD) val tbd: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_NET) val net: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WINDOW) val window: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_ROCKET) val rocket: RocketResponse?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_SUCCESS) val success: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES) val failures: List<LaunchFailures>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_UPCOMING) val upcoming: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DETAILS) val details: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS) val fairings: Fairings?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW) val crew: List<LaunchCrewQueriedResponse>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_SHIPS) val ships: List<ShipQueriedResponse>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CAPSULES) val capsules: List<String>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_PAYLOADS) val payloads: List<PayloadResponse>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_LAUNCHPAD) val launchpad: LaunchpadResponse?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES) val cores: List<LaunchCoreQueriedResponse>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_LINKS) val links: LaunchLinks?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_AUTO_UPDATE) val autoUpdate: Boolean?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class LaunchFailures(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_TIME) val time: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_ALTITUDE) val altitude: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_REASON) val reason: String?
)

data class Fairings(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_REUSED) val reused: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_RECOVERY_ATTEMPT) val recoveryAttempt: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_RECOVERED) val isRecovered: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_SHIPS) val ships: List<String>?
)

data class LaunchCrewResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW) val crew: String,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW_ROLE) val role: String?
)


data class LaunchCrewQueriedResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW) val crew: CrewQueriedResponse,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW_ROLE) val role: String?
)

data class LaunchCoreResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_CORE) var id: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_FLIGHT) var flight: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_GRIDFINS) var gridfins: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LEGS) var legs: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_REUSED) var reused: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_ATTEMPT) var landingAttempt: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_SUCCESS) var landingSuccess: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_TYPE) var landingType: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_PAD) var landingPad: String?
)

data class LaunchCoreQueriedResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_CORE) var core: CoreQueriedResponse?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_FLIGHT) var flight: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_GRIDFINS) var gridfins: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LEGS) var legs: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_REUSED) var reused: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_ATTEMPT) var landingAttempt: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_SUCCESS) var landingSuccess: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_TYPE) var landingType: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES_LANDING_PAD) var landingPad: LandingPadQueriedResponse?
)

data class LaunchLinks(
    @field:Json(name = SPACEX_FIELD_LAUNCH_PATCH) val missionPatch: MissionPatch?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT) val redditLinks: MissionRedditLinks?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLICKR) val flickr: MissionFlickr?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WEBCAST) val webcast: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_YOUTUBE_ID) val youtubeId: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_ARTICLE) val article: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WIKI) val wikipedia: String?
)

data class MissionPatch(
    @field:Json(name = SPACEX_FIELD_LAUNCH_PATCH_SMALL) val patchSmall: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_PATCH_LARGE) val patchLarge: String?
)

data class MissionRedditLinks(
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_CAMPAIGN) val campaign: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_LAUNCH) val launch: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_MEDIA) val media: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_RECOVERY) val recovery: String?
)

data class MissionFlickr(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLICKR_SMALL) val small: List<String>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLICKR_ORIGINAL) val original: List<String>?
)