package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

data class LaunchDocsModel(
    @field:Json(name = "docs") val docs: List<LaunchQueriedResponse>
)

data class LaunchResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER) val flightNumber: Int,
    @field:Json(name = SPACEX_FIELD_LAUNCH_NAME) val missionName: String,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_UTC) val launchDateUtc: String,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_UNIX) val launchDateUnix: Long,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_LOCAL) val launchDateLocal: String,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DATE_PRECISION) val datePrecision: String,
    @field:Json(name = SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UTC) val staticFireDateUtc: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UNIX) val staticFireDateUnix: Long?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_TBD) val tbd: Boolean,
    @field:Json(name = SPACEX_FIELD_LAUNCH_NET) val net: Boolean,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WINDOW) val window: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_ROCKET) val rocket: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_SUCCESS) val success: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES) val failures: List<LaunchFailures>,
    @field:Json(name = SPACEX_FIELD_LAUNCH_UPCOMING) val upcoming: Boolean,
    @field:Json(name = SPACEX_FIELD_LAUNCH_DETAILS) val details: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS) val fairings: Fairings?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW) val crew: List<String>,
    @field:Json(name = SPACEX_FIELD_LAUNCH_SHIPS) val ships: List<String>,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CAPSULES) val capsules: List<String>,
    @field:Json(name = SPACEX_FIELD_LAUNCH_PAYLOADS) val payloads: List<String>,
    @field:Json(name = SPACEX_FIELD_LAUNCH_LAUNCHPAD) val launchpad: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CORES) val cores: List<LaunchCoreResponse>,
    @field:Json(name = SPACEX_FIELD_LAUNCH_LINKS) val links: LaunchLinks?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_AUTO_UPDATE) val autoUpdate: Boolean,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class LaunchQueriedResponse(
    @field:Json(name = "flight_number") val flightNumber: Int?,
    @field:Json(name = "name") val missionName: String?,
    @field:Json(name = "date_utc") val launchDateUtc: String?,
    @field:Json(name = "date_unix") val launchDateUnix: Long?,
    @field:Json(name = "date_local") val launchDateLocal: String?,
    @field:Json(name = "date_precision") val datePrecision: String?,
    @field:Json(name = "static_fire_date_utc") val staticFireDateUtc: String?,
    @field:Json(name = "static_fire_date_unix") val staticFireDateUnix: Long?,
    @field:Json(name = "tbd") val tbd: Boolean?,
    @field:Json(name = "net") val net: Boolean?,
    @field:Json(name = "window") val window: Int?,
    @field:Json(name = "rocket") val rocket: RocketsModel?,
    @field:Json(name = "success") val success: Boolean?,
    @field:Json(name = "failures") val failures: List<LaunchFailures>?,
    @field:Json(name = "upcoming") val upcoming: Boolean?,
    @field:Json(name = "details") val details: String?,
    @field:Json(name = "fairings") val fairings: Fairings?,
    @field:Json(name = "crew") val crew: List<CrewResponse>?,
    @field:Json(name = "ships") val ships: List<Ship>?,
    @field:Json(name = "capsules") val capsules: List<String>?,
    @field:Json(name = "payloads") val payloads: List<PayloadModel>?,
    @field:Json(name = "launchpad") val launchpad: LaunchpadModel?,
    @field:Json(name = "cores") val cores: List<LaunchCoreQueriedResponse>?,
    @field:Json(name = "links") val links: LaunchLinks?,
    @field:Json(name = "auto_update") val autoUpdate: Boolean?,
    @field:Json(name = "id") val id: String
)

@Parcelize
data class LaunchFailures(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_TIME) val time: Int,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_ALTITUDE) val altitude: Int,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_REASON) val reason: String
) : Parcelable

@Parcelize
data class Fairings(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_REUSED) val reused: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_RECOVERY_ATTEMPT) val recoveryAttempt: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_RECOVERED) val isRecovered: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS_SHIPS) val ships: List<String>?
) : Parcelable

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
    @field:Json(name = "core") var core: CoreQueriedResponse?,
    @field:Json(name = "flight") var flight: Int?,
    @field:Json(name = "gridfins") var gridfins: Boolean?,
    @field:Json(name = "legs") var legs: Boolean?,
    @field:Json(name = "reused") var reused: Boolean?,
    @field:Json(name = "landing_attempt") var landingAttempt: Boolean?,
    @field:Json(name = "landing_success") var landingSuccess: Boolean?,
    @field:Json(name = "landing_type") var landingType: String?,
    @field:Json(name = "landpad") var landingPad: LandingPadQueriedResponse?
)

@Parcelize
data class LaunchLinks(
    @field:Json(name = SPACEX_FIELD_LAUNCH_PATCH) val missionPatch: MissionPatch?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT) val redditLinks: MissionRedditLinks?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLICKR) val flickr: MissionFlickr?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_PRESSKIT) val presskit: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WEBCAST) val webcast: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_YOUTUBE_ID) val youtubeId: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_ARTICLE) val article: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WIKI) val wikipedia: String?
) : Parcelable

@Parcelize
data class MissionPatch(
    @field:Json(name = SPACEX_FIELD_LAUNCH_PATCH_SMALL) val patchSmall: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_PATCH_LARGE) val patchLarge: String?
) : Parcelable

@Parcelize
data class MissionRedditLinks(
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_CAMPAIGN) val campaign: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_LAUNCH) val launch: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_MEDIA) val media: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT_RECOVERY) val recovery: String?
) : Parcelable

@Parcelize
data class MissionFlickr(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLICKR_SMALL) val small: List<String>?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLICKR_ORIGINAL) val original: List<String>?
) : Parcelable

@Parcelize
data class Launch(
    val flightNumber: Int?,
    val missionName: String?,
    val launchDateUtc: String?,
    val launchDateUnix: Long?,
    val launchDateLocal: String?,
    val datePrecision: DatePrecision?,
    val staticFireDateUtc: String?,
    val staticFireDateUnix: Long?,
    val tbd: Boolean?,
    val net: Boolean?,
    val window: Int?,
    val rocketId: String? = null,
    val rocket: RocketsModel? = null,
    val success: Boolean?,
    val failures: List<LaunchFailures>?,
    val upcoming: Boolean?,
    val details: String?,
    val fairings: Fairings?,
    val crewIds: List<String>? = null,
    val crew: List<Crew>? = null,
    val shipIds: List<String>? = null,
    val ships: List<Ship>? = null,
    val capsules: List<String>?,
    val payloadIds: List<String>? = null,
    val payloads: List<PayloadModel>? = null,
    val launchpadId: String? = null,
    val launchpad: LaunchpadModel? = null,
    val cores: List<LaunchCore>?,
    val links: LaunchLinks?,
    val autoUpdate: Boolean?,
    val id: String
) : Parcelable {

    constructor(
        response: LaunchResponse
    ) : this(
        flightNumber = response.flightNumber,
        missionName = response.missionName,
        launchDateUtc = response.launchDateUtc,
        launchDateUnix = response.launchDateUnix,
        launchDateLocal = response.launchDateLocal,
        datePrecision = response.datePrecision.toDatePrecision(),
        staticFireDateUtc = response.staticFireDateUtc,
        staticFireDateUnix = response.staticFireDateUnix,
        tbd = response.tbd,
        net = response.net,
        window = response.window,
        rocketId = response.rocket,
        success = response.success,
        failures = response.failures,
        upcoming = response.upcoming,
        details = response.details,
        fairings = response.fairings,
        crewIds = response.crew,
        shipIds = response.ships,
        capsules = response.capsules,
        payloadIds = response.payloads,
        launchpadId = response.launchpad,
        cores = response.cores.map { LaunchCore(it) },
        links = response.links,
        autoUpdate = response.autoUpdate,
        id = response.id
    )

    constructor(
        response: LaunchQueriedResponse
    ) : this(
        flightNumber = response.flightNumber,
        missionName = response.missionName,
        launchDateUtc = response.launchDateUtc,
        launchDateUnix = response.launchDateUnix,
        launchDateLocal = response.launchDateLocal,
        datePrecision = response.datePrecision.toDatePrecision(),
        staticFireDateUtc = response.staticFireDateUtc,
        staticFireDateUnix = response.staticFireDateUnix,
        tbd = response.tbd,
        net = response.net,
        window = response.window,
        rocketId = response.rocket?.id,
        rocket = response.rocket,
        success = response.success,
        failures = response.failures,
        upcoming = response.upcoming,
        details = response.details,
        fairings = response.fairings,
        crew = response.crew?.map { Crew(it) },
        ships = response.ships,
        capsules = response.capsules,
        payloads = response.payloads,
        launchpad = response.launchpad,
        cores = response.cores?.map { LaunchCore(it) },
        links = response.links,
        autoUpdate = response.autoUpdate,
        id = response.id
    )

    companion object {
        private fun String?.toDatePrecision() = when (this) {
            SPACEX_LAUNCH_DATE_PRECISION_HALF -> DatePrecision.HALF
            SPACEX_LAUNCH_DATE_PRECISION_QUARTER -> DatePrecision.QUARTER
            SPACEX_LAUNCH_DATE_PRECISION_YEAR -> DatePrecision.YEAR
            SPACEX_LAUNCH_DATE_PRECISION_MONTH -> DatePrecision.MONTH
            SPACEX_LAUNCH_DATE_PRECISION_DAY -> DatePrecision.DAY
            SPACEX_LAUNCH_DATE_PRECISION_HOUR -> DatePrecision.HOUR
            else -> null
        }
    }
}

@Parcelize
data class LaunchCore(
    var id: String?,
    var core: Core? = null,
    var flight: Int?,
    var gridfins: Boolean?,
    var legs: Boolean?,
    var reused: Boolean?,
    var landingAttempt: Boolean?,
    var landingSuccess: Boolean?,
    var landingType: String?,
    var landingPadId: String? = null,
    var landingPad: LandingPad? = null
) : Parcelable {

    constructor(
        response: LaunchCoreResponse
    ) : this(
        id = response.id,
        flight = response.flight,
        gridfins = response.gridfins,
        legs = response.legs,
        reused = response.reused,
        landingAttempt = response.landingAttempt,
        landingSuccess = response.landingSuccess,
        landingType = response.landingType,
        landingPadId = response.landingPad
    )

    constructor(
        response: LaunchCoreQueriedResponse
    ) : this(
        id = response.core?.id,
        core = response.core?.let { Core(it) },
        flight = response.flight,
        gridfins = response.gridfins,
        legs = response.legs,
        reused = response.reused,
        landingAttempt = response.landingAttempt,
        landingSuccess = response.landingSuccess,
        landingType = response.landingType,
        landingPad = response.landingPad?.let { LandingPad(it) }
    )

}

enum class DatePrecision {
    HALF,
    QUARTER,
    YEAR,
    MONTH,
    DAY,
    HOUR
}