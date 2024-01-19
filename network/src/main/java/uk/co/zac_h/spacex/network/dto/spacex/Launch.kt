package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class LaunchResponse(
    val id: String,
    val url: String?,
    val slug: String?,
    val name: String,
    val status: Status?,
    @field:Json(name = "last_updated") val lastUpdated: String?,
    val net: String?,
    @field:Json(name = "window_end") val windowEnd: String?,
    @field:Json(name = "window_start") val windowStart: String?,
    val probability: Int?,
    val holdReason: String?,
    val failReason: String?,
    val hashtag: String?,
    @field:Json(name = "launch_service_provider") val launchServiceProvider: LaunchServiceProvider?,
    val rocket: Rocket?,
    val mission: Mission?,
    val pad: Pad?,
    @field:Json(name = "vidURLs") val video: List<Video>?,
    @field:Json(name = "webcast_live") val webcastLive: Boolean?,
    val image: String?,
    val infographic: String?,
    val program: List<Program>?,
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
        val id: Int,
        val name: String?,
        val abbrev: String?,
        val description: String?
    )

    data class LaunchServiceProvider(
        val id: Int,
        val url: String?,
        val name: String?,
        val type: String?
    )

    data class Rocket(
        val id: Int,
        val configuration: Configuration?,
        @field:Json(name = "launcher_stage") val launcherStage: List<LauncherStage?>?,
        @field:Json(name = "spacecraft_stage") val spacecraftStage: SpacecraftStage?
    ) {

        data class Configuration(
            val id: Int,
            val url: String?,
            val name: String?,
            val family: String?,
            @field:Json(name = "full_name") val fullName: String?,
            val variant: String?
        )

        data class LauncherStage(
            val id: Int,
            val type: String?,
            val reused: Boolean?,
            val launcher: Launcher?,
            val landing: Landing?,
            @field:Json(name = "turn_around_time_days") val turnAroundTimeDays: Int?,
            @field:Json(name = "previous_flight") val previousFlight: PreviousFlight?
        ) {

            data class Launcher(
                val id: Int,
                @field:Json(name = "serial_number") val serialNumber: String?,
                val status: String?,
                val flights: Int?,
            )

            data class Landing(
                val id: Int,
                val attempt: Boolean?,
                val success: Boolean?,
                val description: String?,
                val location: Location?,
                val type: Type?,
            ) {

                data class Location(
                    val id: Int,
                    val name: String?,
                    val abbrev: String?,
                    val description: String?,
                    @field:Json(name = "successful_landings") val successfulLandings: Int?,
                )

                data class Type(
                    val id: Int,
                    val name: String?,
                    val abbrev: String?,
                    val description: String?
                )
            }

            data class PreviousFlight(
                val id: String,
                val name: String?
            )
        }

        data class SpacecraftStage(
            val id: Int,
            val destination: String?,
            @field:Json(name = "launch_crew") val launchCrew: List<LaunchCrew?>?
        ) {

            data class LaunchCrew(
                val id: Int,
                val role: Role?,
                val astronaut: Astronaut?,
            ) {

                data class Role(
                    val id: Int,
                    val role: String?,
                    val priority: Int?
                )

                data class Astronaut(
                    val id: Int,
                    val name: String?,
                    val status: Status?,
                    val agency: Agency?,
                    val bio: String?,
                    @field:Json(name = "profile_image") val profileImage: String?,
                    @field:Json(name = "last_flight") val lastFlight: String?,
                    @field:Json(name = "first_flight") val firstFlight: String?
                ) {

                    data class Status(
                        val id: Int,
                        val name: String?,
                    )

                    data class Agency(
                        val id: Int,
                        val name: String?
                    )
                }
            }
        }
    }

    data class Mission(
        val id: Int,
        val name: String?,
        val description: String?,
        @field:Json(name = "launch_designator") val launchDesignator: String?,
        val type: String?,
        val orbit: Orbit?
    ) {

        data class Orbit(
            val id: Int,
            val name: String?,
            val abbrev: String?
        )
    }

    data class Pad(
        val id: Int,
        val url: String?,
        @field:Json(name = "agency_id") val agencyId: String?,
        val name: String?,
        @field:Json(name = "info_url") val infoUrl: String?,
        @field:Json(name = "wiki_url") val wikiUrl: String?,
        @field:Json(name = "map_url") val mapUrl: String?,
        val latitude: String?,
        val longitude: String?,
        val location: Location,
        @field:Json(name = "map_image") val mapImage: String?,
        @field:Json(name = "total_launch_count") val totalLaunchCount: Int?,
        @field:Json(name = "orbital_launch_attempt_count") val orbitalLaunchAttemptCount: Int?,
    ) {

        data class Location(
            val id: Int,
            val url: String?,
            val name: String?,
            @field:Json(name = "country_code") val countryCode: String?,
            @field:Json(name = "map_image") val mapImage: String?,
            @field:Json(name = "total_launch_count") val totalLaunchCount: Int?,
            @field:Json(name = "total_landing_count") val totalLandingCount: Int?
        )
    }

    data class Video(
        val priority: Int,
        val source: String?,
        val title: String?,
        val description: String?,
        @field:Json(name = "feature_image") val featureImage: String?,
        val url: String,
        val type: Type?,
    ) {

        data class Type(
            val id: Int,
            val name: String,
        )
    }

    data class Program(
        val id: Int,
        val url: String?,
        val name: String?,
        val description: String?,
        val agencies: List<Agency>?,
        @field:Json(name = "image_url") val imageUrl: String?,
        @field:Json(name = "start_date") val startDate: String?,
        @field:Json(name = "end_date") val endDate: String?,
        @field:Json(name = "info_url") val infoUrl: String?,
        @field:Json(name = "wiki_url") val wikiUrl: String?,
        @field:Json(name = "mission_patches") val missionPatches: List<MissionPatch>?
    ) {

        data class Agency(
            val id: Int,
            val url: String?,
            val name: String?,
            val type: String?
        )
    }

    data class MissionPatch(
        val id: Int,
        val name: String?,
        val priority: Int?,
        @field:Json(name = "image_url") val imageUrl: String?,
        val agency: Program.Agency?,
    )
}
