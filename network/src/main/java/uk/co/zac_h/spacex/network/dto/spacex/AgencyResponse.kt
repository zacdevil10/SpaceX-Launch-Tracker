package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json

data class AgencyResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "administrator") val administrator: String?,
    @field:Json(name = "founding_year") val foundingYear: String?,
    @field:Json(name = "total_launch_count") val totalLaunchCount: Int?,
    @field:Json(name = "successful_launches") val successfulLaunches: Int?,
    @field:Json(name = "consecutive_successful_launches") val consecutiveSuccessfulLaunches: Int?,
    @field:Json(name = "failed_launches") val failedLaunches: Int?,
    @field:Json(name = "pending_launches") val pendingLaunches: Int?,
    @field:Json(name = "successful_landings") val successfulLandings: Int?,
    @field:Json(name = "failed_landings") val failedLandings: Int?,
    @field:Json(name = "attempted_landings") val attemptedLandings: Int?,
    @field:Json(name = "consecutive_successful_landings") val consecutiveSuccessfulLandings: Int?,
    @field:Json(name = "info_url") val infoUrl: String?,
    @field:Json(name = "wiki_url") val wikiUrl: String?,
    @field:Json(name = "launcher_list") val launcherList: List<Launcher>?,
    @field:Json(name = "spacecraft_list") val spacecraftList: List<Spacecraft>?
) {

    data class Launcher(
        @field:Json(name = "id") val id: Int,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "description") val description: String?,
        @field:Json(name = "full_name") val fullName: String?,
        @field:Json(name = "variant") val variant: String?,
        @field:Json(name = "min_stage") val minStage: Int?,
        @field:Json(name = "max_stage") val maxStage: Int?,
        @field:Json(name = "length") val length: Float?,
        @field:Json(name = "diameter") val diameter: Float?,
        @field:Json(name = "maiden_flight") val maidenFlight: String?,
        @field:Json(name = "launch_mass") val launchMass: Int?,
        @field:Json(name = "leo_capacity") val leoCapacity: Int?,
        @field:Json(name = "gto_capacity") val gtoCapacity: Int?,
        @field:Json(name = "to_thrust") val toThrust: Int?,
        @field:Json(name = "apogee") val apogee: Int?,
        @field:Json(name = "image_url") val imageUrl: String?,
        @field:Json(name = "info_url") val infoUrl: String?,
        @field:Json(name = "wiki_url") val wikiUrl: String?,
        @field:Json(name = "consecutive_successful_launches") val consecutiveSuccessfulLaunches: Int?,
        @field:Json(name = "successful_launches") val successfulLaunches: Int?,
        @field:Json(name = "failed_launches") val failedLaunches: Int?,
        @field:Json(name = "pending_launches") val pendingLaunches: Int?
    )

    data class Spacecraft(
        @field:Json(name = "id") val id: String,
        @field:Json(name = "name") val name: String?,
        @field:Json(name = "in_use") val inUse: Boolean?,
        @field:Json(name = "capability") val capability: String?,
        @field:Json(name = "history") val history: String?,
        @field:Json(name = "details") val details: String?,
        @field:Json(name = "maiden_flight") val maidenFlight: String?,
        @field:Json(name = "height") val height: Float?,
        @field:Json(name = "diameter") val diameter: Float?,
        @field:Json(name = "human_rated") val humanRated: Boolean?,
        @field:Json(name = "crew_capacity") val crewCapacity: Int?,
        @field:Json(name = "payload_capacity") val payloadCapacity: Int?,
        @field:Json(name = "flight_life") val flightLife: String?,
        @field:Json(name = "image_url") val imageUrl: String?,
        @field:Json(name = "wiki_link") val wikiLink: String?,
        @field:Json(name = "info_link") val infoLink: String?
    )
}