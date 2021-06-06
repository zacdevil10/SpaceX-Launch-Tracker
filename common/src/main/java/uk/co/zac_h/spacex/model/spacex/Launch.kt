package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import uk.co.zac_h.spacex.utils.*

data class LaunchDocsModel(
    @field:Json(name = "docs") val docs: List<LaunchQueriedResponse>
)

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
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS) val fairings: List<Fairings>?,
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
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRINGS) val fairings: List<Fairings>?,
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

@Parcelize
data class LaunchFailures(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_TIME) val time: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_ALTITUDE) val altitude: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAILURES_REASON) val reason: String?
) : Parcelable

@Parcelize
data class Fairings(
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING) val fairing: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_FLIGHT) val flight: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_REUSED) val reused: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_NET_ATTEMPT) val netAttempt: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_NET_LANDING) val netLanding: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_WATER_ATTEMPT) val waterAttempt: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_WATER_LANDING) val waterLanding: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_RECOVERED) val recovered: Boolean?,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FAIRING_SHIPS) val ships: List<String>?
) : Parcelable

data class LaunchCrewResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW) val crew: String,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW_ROLE) val role: String
)


data class LaunchCrewQueriedResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW) val crew: CrewQueriedResponse,
    @field:Json(name = SPACEX_FIELD_LAUNCH_CREW_ROLE) val role: String
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
    val launchDate: EventDate?,
    val datePrecision: DatePrecision?,
    val staticFireDate: EventDate?,
    val tbd: Boolean?,
    val net: Boolean?,
    val window: Int?,
    val rocketId: String? = null,
    val rocket: Rocket? = null,
    val success: Boolean?,
    val failures: List<LaunchFailures>?,
    val upcoming: Boolean?,
    val details: String?,
    val fairings: List<Fairings>?,
    val crew: List<Crew>?,
    val shipIds: List<String>? = null,
    val ships: List<Ship>? = null,
    val capsules: List<String>?,
    val payloadIds: List<String>? = null,
    val payloads: List<Payload>? = null,
    val launchpadId: String? = null,
    val launchpad: Launchpad? = null,
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
        launchDate = EventDate(
            dateUtc = response.launchDateUtc,
            dateUnix = response.launchDateUnix,
            dateLocal = response.launchDateLocal
        ),
        datePrecision = response.datePrecision.toDatePrecision(),
        staticFireDate = EventDate(
            dateUtc = response.staticFireDateUtc,
            dateUnix = response.staticFireDateUnix
        ),
        tbd = response.tbd,
        net = response.net,
        window = response.window,
        rocketId = response.rocket,
        success = response.success,
        failures = response.failures,
        upcoming = response.upcoming,
        details = response.details,
        fairings = response.fairings,
        crew = response.crew?.map { Crew(it) },
        shipIds = response.ships,
        capsules = response.capsules,
        payloadIds = response.payloads,
        launchpadId = response.launchpad,
        cores = response.cores?.map { LaunchCore(it) },
        links = response.links,
        autoUpdate = response.autoUpdate,
        id = response.id
    )

    constructor(
        response: LaunchQueriedResponse
    ) : this(
        flightNumber = response.flightNumber,
        missionName = response.missionName,
        launchDate = EventDate(
            dateUtc = response.launchDateUtc,
            dateUnix = response.launchDateUnix,
            dateLocal = response.launchDateLocal
        ),
        datePrecision = response.datePrecision.toDatePrecision(),
        staticFireDate = EventDate(
            dateUtc = response.staticFireDateUtc,
            dateUnix = response.staticFireDateUnix
        ),
        tbd = response.tbd,
        net = response.net,
        window = response.window,
        rocketId = response.rocket?.id,
        rocket = response.rocket?.let { Rocket(it) },
        success = response.success,
        failures = response.failures,
        upcoming = response.upcoming,
        details = response.details,
        fairings = response.fairings,
        crew = response.crew?.map { Crew(it) },
        ships = response.ships?.map { Ship(it) },
        capsules = response.capsules,
        payloads = response.payloads?.map { Payload(it) },
        launchpad = response.launchpad?.let { Launchpad(it) },
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

enum class DatePrecision(val precision: String) {
    HALF("yyyy"),
    QUARTER("yyyy"),
    YEAR("yyyy"),
    MONTH("MMM yyyy"),
    DAY("dd MMM yyyy"),
    HOUR("dd MMM yy - HH:mm zzz")
}

enum class Upcoming(val text: String, val upcoming: Boolean) {
    NEXT("next", true), LATEST("latest", false)
}