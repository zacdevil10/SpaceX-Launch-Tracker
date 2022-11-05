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
    @field:Json(name = "wiki_url") val wikiUrl: String?
)