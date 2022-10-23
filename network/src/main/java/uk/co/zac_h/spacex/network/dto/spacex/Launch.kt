package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.network.*

data class LaunchResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "slug") val slug: String?,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "status") val status: Status?,
    @field:Json(name = "last_updated") val lastUpdated: String?,
    @field:Json(name = "net") val net: String?,
    @field:Json(name = "window_end") val windowEnd: String?,
    @field:Json(name = "window_start") val windowStart: String?,
    @field:Json(name = "probability") val probability: Int?,
    @field:Json(name = "holdreason") val holdReason: String?,
    @field:Json(name = "failreason") val failReason: String?,
    @field:Json(name = "hashtag") val hashtag: String?,
    @field:Json(name = "launch_service_provider") val launchServiceProvider: LaunchServiceProvider?,
    @field:Json(name = "rocket") val rocket: Rocket?,
    @field:Json(name = "mission") val mission: Mission?,
    @field:Json(name = "pad") val pad: Pad?,
    @field:Json(name = "vidURLs") val video: List<Video>?,
    @field:Json(name = "webcast_live") val webcastLive: Boolean?,
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "infographic") val infographic: String?,
    @field:Json(name = "program") val program: List<Program>?,
    @field:Json(name = "orbital_launch_attempt_count") val orbitalLaunchAttemptCount: Int?,
    @field:Json(name = "location_launch_attempt_count") val locationLaunchAttemptCount: Int?,
    @field:Json(name = "pad_launch_attempt_count") val padLaunchAttemptCount: Int?,
    @field:Json(name = "agency_launch_attempt_count") val agencyLaunchAttemptCount: Int?,
    @field:Json(name = "orbital_launch_attempt_count_year") val orbitalLaunchAttemptCountYear: Int?,
    @field:Json(name = "location_launch_attempt_count_year") val locationLaunchAttemptCountYear: Int?,
    @field:Json(name = "pad_launch_attempt_count_year") val padLaunchAttemptCountYear: Int?,
    @field:Json(name = "agency_launch_attempt_count_year") val agencyLaunchAttemptCountYear: Int?,
    @field:Json(name = "mission_patches") val missionPatches: List<MissionPatch>?,
) {

    data class Status(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "abbrev") val abbrev: String?,
        @field:Json(name = "description") val description: String?
    )

    data class LaunchServiceProvider(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "url") val url: String?,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "type") val type: String?
    )

    data class Rocket(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "configuration") val configuration: Configuration?,
        @field:Json(name = "launcher_stage") val launcherStage: List<LauncherStage?>?,
        @field:Json(name = "spacecraft_stage") val spacecraftStage: SpacecraftStage?
    ) {

        data class Configuration(
            @field:Json(name = "id") val id: Int,
            @field:Json(name = "url") val url: String?,
            @field:Json(name = "name") val name: String?,
            @field:Json(name = "family") val family: String?,
            @field:Json(name = "full_name") val fullName: String?,
            @field:Json(name = "variant") val variant: String?
        )

        data class LauncherStage(
            @field:Json(name = "id") val id: Int,
            @field:Json(name = "type") val type: String?,
            @field:Json(name = "reused") val reused: Boolean?,
            @field:Json(name = "launcher_flight_number") val launcherFlightNumber: Int?,
            @field:Json(name = "launcher") val launcher: Launcher?,
            @field:Json(name = "landing") val landing: Landing?,
        ) {

            data class Launcher(
                @field:Json(name = "id") val id: Int,
                @field:Json(name = "serial_number") val serialNumber: String?,
                @field:Json(name = "status") val status: String?,
                @field:Json(name = "flights") val flights: Int?,
            )

            data class Landing(
                @field:Json(name = "id") val id: Int,
                @field:Json(name = "attempt") val attempt: Boolean?,
                @field:Json(name = "success") val success: Boolean?,
                @field:Json(name = "description") val description: String?,
                @field:Json(name = "location") val location: Location?,
                @field:Json(name = "type") val type: Type?,
            ) {

                data class Location(
                    @field:Json(name = "id") val id: Int,
                    @field:Json(name = "name") val name: String?,
                    @field:Json(name = "abbrev") val abbrev: String?,
                    @field:Json(name = "description") val description: String?,
                    @field:Json(name = "successful_landings") val successfulLandings: Int?,
                )

                data class Type(
                    @field:Json(name = "id") val id: Int,
                    @field:Json(name = "name") val name: String?,
                    @field:Json(name = "abbrev") val abbrev: String?,
                    @field:Json(name = "description") val description: String?
                )
            }
        }

        data class SpacecraftStage(
            @field:Json(name = "id") val id: Int,
            @field:Json(name = "destination") val destination: String?,
            @field:Json(name = "launch_crew") val launchCrew: List<LaunchCrew?>?
        ) {

            data class LaunchCrew(
                @field:Json(name = "id") val id: Int,
                @field:Json(name = "role") val role: Role?,
                @field:Json(name = "astronaut") val astronaut: Astronaut?,
            ) {

                data class Role(
                    @field:Json(name = "id") val id: Int,
                    @field:Json(name = "role") val role: String?,
                    @field:Json(name = "priority") val priority: Int?
                )

                data class Astronaut(
                    @field:Json(name = "id") val id: Int,
                    @field:Json(name = "name") val name: String?,
                    @field:Json(name = "status") val status: Status?,
                    @field:Json(name = "agency") val agency: Agency?,
                    @field:Json(name = "bio") val bio: String?,
                    @field:Json(name = "profile_image") val profileImage: String?,
                    @field:Json(name = "last_flight") val lastFlight: String?,
                    @field:Json(name = "first_flight") val firstFlight: String?
                ) {

                    data class Status(
                        @field:Json(name = "id") val id: Int,
                        @field:Json(name = "name") val name: String?,
                    )

                    data class Agency(
                        @field:Json(name = "id") val id: Int,
                        @field:Json(name = "name") val name: String?
                    )
                }
            }
        }
    }

    data class Mission(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "description") val description: String?,
        @field:Json(name = "launch_designator") val launchDesignator: String?,
        @field:Json(name = "type") val type: String?,
        @field:Json(name = "orbit") val orbit: Orbit?
    ) {

        data class Orbit(
            @field:Json(name = "id") val id: Int,
            @field:Json(name = "name") val name: String?,
            @field:Json(name = "abbrev") val abbrev: String?
        )
    }

    data class Pad(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "url") val url: String?,
        @field:Json(name = "agency_id") val agencyId: String?,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "info_url") val infoUrl: String?,
        @field:Json(name = "wiki_url") val wikiUrl: String?,
        @field:Json(name = "map_url") val mapUrl: String?,
        @field:Json(name = "latitude") val latitude: String?,
        @field:Json(name = "longitude") val longitude: String?,
        @field:Json(name = "location") val location: Location,
        @field:Json(name = "map_image") val mapImage: String?,
        @field:Json(name = "total_launch_count") val totalLaunchCount: Int?,
        @field:Json(name = "orbital_launch_attempt_count") val orbitalLaunchAttemptCount: Int?,
    ) {

        data class Location(
            @field:Json(name = "id") val id: Int,
            @field:Json(name = "url") val url: String?,
            @field:Json(name = "name") val name: String?,
            @field:Json(name = "country_code") val countryCode: String?,
            @field:Json(name = "map_image") val mapImage: String?,
            @field:Json(name = "total_launch_count") val totalLaunchCount: Int?,
            @field:Json(name = "total_landing_count") val totalLandingCount: Int?
        )
    }

    data class Video(
        @field:Json(name = "priority") val priority: Int?,
        @field:Json(name = "title") val title: String?,
        @field:Json(name = "description") val description: String?,
        @field:Json(name = "feature_image") val featureImage: String?,
        @field:Json(name = "url") val url: String?,
    )

    data class Program(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "url") val url: String?,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "description") val description: String?,
        @field:Json(name = "agencies") val agencies: List<Agency>?,
        @field:Json(name = "image_url") val imageUrl: String?,
        @field:Json(name = "start_date") val startDate: String?,
        @field:Json(name = "end_date") val endDate: String?,
        @field:Json(name = "info_url") val infoUrl: String?,
        @field:Json(name = "wiki_url") val wikiUrl: String?,
        @field:Json(name = "mission_patches") val missionPatches: List<MissionPatch>?
    ) {

        data class Agency(
            @field:Json(name = "id") val id: Int,
            @field:Json(name = "url") val url: String?,
            @field:Json(name = "name") val name: String?,
            @field:Json(name = "type") val type: String?
        )
    }

    data class MissionPatch(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "priority") val priority: Int?,
        @field:Json(name = "image_url") val imageUrl: String?,
        @field:Json(name = "agency") val agency: Program.Agency?,
    )
}

//TODO Remove once all usages are migrated to launch library
data class LegacyLaunchResponse(
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
    @field:Json(name = SPACEX_FIELD_ID) val id: String?
)

data class LegacyLaunchQueriedResponse(
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
    @field:Json(name = SPACEX_FIELD_ID) val id: String?
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
    @field:Json(name = SPACEX_FIELD_LAUNCH_PATCH) val missionPatch: MissionPatch? = null,
    @field:Json(name = SPACEX_FIELD_LAUNCH_REDDIT) val redditLinks: MissionRedditLinks? = null,
    @field:Json(name = SPACEX_FIELD_LAUNCH_FLICKR) val flickr: MissionFlickr? = null,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WEBCAST) val webcast: String? = null,
    @field:Json(name = SPACEX_FIELD_LAUNCH_YOUTUBE_ID) val youtubeId: String? = null,
    @field:Json(name = SPACEX_FIELD_LAUNCH_ARTICLE) val article: String? = null,
    @field:Json(name = SPACEX_FIELD_LAUNCH_WIKI) val wikipedia: String? = null
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
