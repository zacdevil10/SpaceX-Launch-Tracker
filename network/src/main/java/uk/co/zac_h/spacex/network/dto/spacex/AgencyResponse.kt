package uk.co.zac_h.spacex.network.dto.spacex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgencyResponse(
    val id: Int,
    val name: String?,
    val description: String?,
    val administrator: String?,
    @SerialName("founding_year") val foundingYear: String?,
    @SerialName("total_launch_count") val totalLaunchCount: Int?,
    @SerialName("successful_launches") val successfulLaunches: Int?,
    @SerialName("consecutive_successful_launches") val consecutiveSuccessfulLaunches: Int?,
    @SerialName("failed_launches") val failedLaunches: Int?,
    @SerialName("pending_launches") val pendingLaunches: Int?,
    @SerialName("successful_landings") val successfulLandings: Int?,
    @SerialName("failed_landings") val failedLandings: Int?,
    @SerialName("attempted_landings") val attemptedLandings: Int?,
    @SerialName("consecutive_successful_landings") val consecutiveSuccessfulLandings: Int?,
    @SerialName("info_url") val infoUrl: String?,
    @SerialName("wiki_url") val wikiUrl: String?,
    @SerialName("launcher_list") val launcherList: List<Launcher>?,
    @SerialName("spacecraft_list") val spacecraftList: List<Spacecraft>?
) {

    @Serializable
    data class Launcher(
        val id: Int,
        val name: String?,
        val description: String?,
        val family: String?,
        @SerialName("full_name") val fullName: String?,
        val variant: String?,
        @SerialName("min_stage") val minStage: Int?,
        @SerialName("max_stage") val maxStage: Int?,
        val length: Float?,
        val diameter: Float?,
        @SerialName("maiden_flight") val maidenFlight: String?,
        @SerialName("launch_mass") val launchMass: Int?,
        @SerialName("leo_capacity") val leoCapacity: Int?,
        @SerialName("gto_capacity") val gtoCapacity: Int?,
        @SerialName("to_thrust") val toThrust: Int?,
        val apogee: Int?,
        @SerialName("image_url") val imageUrl: String?,
        @SerialName("info_url") val infoUrl: String?,
        @SerialName("wiki_url") val wikiUrl: String?,
        @SerialName("consecutive_successful_launches") val consecutiveSuccessfulLaunches: Int?,
        @SerialName("successful_launches") val successfulLaunches: Int?,
        @SerialName("failed_launches") val failedLaunches: Int?,
        @SerialName("pending_launches") val pendingLaunches: Int?
    )

    @Serializable
    data class Spacecraft(
        val id: Int,
        val name: String?,
        @SerialName("in_use") val inUse: Boolean?,
        val capability: String?,
        val history: String?,
        val details: String?,
        @SerialName("maiden_flight") val maidenFlight: String?,
        val height: Float?,
        val diameter: Float?,
        @SerialName("human_rated") val humanRated: Boolean?,
        @SerialName("crew_capacity") val crewCapacity: Int?,
        @SerialName("payload_capacity") val payloadCapacity: Int?,
        @SerialName("flight_life") val flightLife: String?,
        @SerialName("image_url") val imageUrl: String?,
        @SerialName("wiki_link") val wikiLink: String?,
        @SerialName("info_link") val infoLink: String?
    )
}