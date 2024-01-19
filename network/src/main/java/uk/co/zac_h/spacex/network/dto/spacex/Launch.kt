package uk.co.zac_h.spacex.network.dto.spacex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchResponse(
    val id: String,
    val url: String?,
    val slug: String?,
    val name: String,
    val status: Status?,
    @SerialName("last_updated") val lastUpdated: String?,
    val net: String?,
    @SerialName("window_end") val windowEnd: String?,
    @SerialName("window_start") val windowStart: String?,
    val probability: Int?,
    val holdReason: String?,
    val failReason: String?,
    val hashtag: String?,
    @SerialName("launch_service_provider") val launchServiceProvider: LaunchServiceProvider?,
    val rocket: Rocket?,
    val mission: Mission?,
    val pad: Pad?,
    @SerialName("vidURLs") val video: List<Video>?,
    @SerialName("webcast_live") val webcastLive: Boolean?,
    val image: String?,
    val infographic: String?,
    val program: List<Program>?,
    @SerialName("orbital_launch_attempt_count") val orbitalLaunchAttemptCount: Int?,
    @SerialName("location_launch_attempt_count") val locationLaunchAttemptCount: Int?,
    @SerialName("pad_launch_attempt_count") val padLaunchAttemptCount: Int?,
    @SerialName("agency_launch_attempt_count") val agencyLaunchAttemptCount: Int?,
    @SerialName("orbital_launch_attempt_count_year") val orbitalLaunchAttemptCountYear: Int?,
    @SerialName("location_launch_attempt_count_year") val locationLaunchAttemptCountYear: Int?,
    @SerialName("pad_launch_attempt_count_year") val padLaunchAttemptCountYear: Int?,
    @SerialName("agency_launch_attempt_count_year") val agencyLaunchAttemptCountYear: Int?,
    @SerialName("mission_patches") val missionPatches: List<MissionPatch>?,
) {

    @Serializable
    data class Status(
        val id: Int,
        val name: String?,
        val abbrev: String?,
        val description: String?
    )

    @Serializable
    data class LaunchServiceProvider(
        val id: Int,
        val url: String?,
        val name: String?,
        val type: String?
    )

    @Serializable
    data class Rocket(
        val id: Int,
        val configuration: Configuration?,
        @SerialName("launcher_stage") val launcherStage: List<LauncherStage?>?,
        @SerialName("spacecraft_stage") val spacecraftStage: SpacecraftStage?
    ) {

        @Serializable
        data class Configuration(
            val id: Int,
            val url: String?,
            val name: String?,
            val family: String?,
            @SerialName("full_name") val fullName: String?,
            val variant: String?
        )

        @Serializable
        data class LauncherStage(
            val id: Int,
            val type: String?,
            val reused: Boolean?,
            val launcher: Launcher?,
            val landing: Landing?,
            @SerialName("turn_around_time_days") val turnAroundTimeDays: Int?,
            @SerialName("previous_flight") val previousFlight: PreviousFlight?
        ) {

            @Serializable
            data class Launcher(
                val id: Int,
                @SerialName("serial_number") val serialNumber: String?,
                val status: String?,
                val flights: Int?,
            )

            @Serializable
            data class Landing(
                val id: Int,
                val attempt: Boolean?,
                val success: Boolean?,
                val description: String?,
                val location: Location?,
                val type: Type?,
            ) {

                @Serializable
                data class Location(
                    val id: Int,
                    val name: String?,
                    val abbrev: String?,
                    val description: String?,
                    @SerialName("successful_landings") val successfulLandings: Int?,
                )

                @Serializable
                data class Type(
                    val id: Int,
                    val name: String?,
                    val abbrev: String?,
                    val description: String?
                )
            }

            @Serializable
            data class PreviousFlight(
                val id: String,
                val name: String?
            )
        }

        @Serializable
        data class SpacecraftStage(
            val id: Int,
            val destination: String?,
            @SerialName("launch_crew") val launchCrew: List<LaunchCrew?>?
        ) {

            @Serializable
            data class LaunchCrew(
                val id: Int,
                val role: Role?,
                val astronaut: Astronaut?,
            ) {

                @Serializable
                data class Role(
                    val id: Int,
                    val role: String?,
                    val priority: Int?
                )

                @Serializable
                data class Astronaut(
                    val id: Int,
                    val name: String?,
                    val status: Status?,
                    val agency: Agency?,
                    val bio: String?,
                    @SerialName("profile_image") val profileImage: String?,
                    @SerialName("last_flight") val lastFlight: String?,
                    @SerialName("first_flight") val firstFlight: String?
                ) {

                    @Serializable
                    data class Status(
                        val id: Int,
                        val name: String?,
                    )

                    @Serializable
                    data class Agency(
                        val id: Int,
                        val name: String?
                    )
                }
            }
        }
    }

    @Serializable
    data class Mission(
        val id: Int,
        val name: String?,
        val description: String?,
        @SerialName("launch_designator") val launchDesignator: String?,
        val type: String?,
        val orbit: Orbit?
    ) {

        @Serializable
        data class Orbit(
            val id: Int,
            val name: String?,
            val abbrev: String?
        )
    }

    @Serializable
    data class Pad(
        val id: Int,
        val url: String?,
        @SerialName("agency_id") val agencyId: Int?,
        val name: String?,
        @SerialName("info_url") val infoUrl: String?,
        @SerialName("wiki_url") val wikiUrl: String?,
        @SerialName("map_url") val mapUrl: String?,
        val latitude: String?,
        val longitude: String?,
        val location: Location,
        @SerialName("map_image") val mapImage: String?,
        @SerialName("total_launch_count") val totalLaunchCount: Int?,
        @SerialName("orbital_launch_attempt_count") val orbitalLaunchAttemptCount: Int?,
    ) {

        @Serializable
        data class Location(
            val id: Int,
            val url: String?,
            val name: String?,
            @SerialName("country_code") val countryCode: String?,
            @SerialName("map_image") val mapImage: String?,
            @SerialName("total_launch_count") val totalLaunchCount: Int?,
            @SerialName("total_landing_count") val totalLandingCount: Int?
        )
    }

    @Serializable
    data class Video(
        val priority: Int,
        val source: String?,
        val title: String?,
        val description: String?,
        @SerialName("feature_image") val featureImage: String?,
        val url: String,
        val type: Type?,
    ) {

        @Serializable
        data class Type(
            val id: Int,
            val name: String,
        )
    }

    @Serializable
    data class Program(
        val id: Int,
        val url: String?,
        val name: String?,
        val description: String?,
        val agencies: List<Agency>?,
        @SerialName("image_url") val imageUrl: String?,
        @SerialName("start_date") val startDate: String?,
        @SerialName("end_date") val endDate: String?,
        @SerialName("info_url") val infoUrl: String?,
        @SerialName("wiki_url") val wikiUrl: String?,
        @SerialName("mission_patches") val missionPatches: List<MissionPatch>?
    ) {

        @Serializable
        data class Agency(
            val id: Int,
            val url: String?,
            val name: String?,
            val type: String?
        )
    }

    @Serializable
    data class MissionPatch(
        val id: Int,
        val name: String?,
        val priority: Int?,
        @SerialName("image_url") val imageUrl: String?,
        val agency: Program.Agency?,
    )
}
