package uk.co.zac_h.spacex.model.spacex

import com.squareup.moshi.Json

// v4
data class LandingPadModel(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "full_name") val fullName: String?,
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "locality") val locality: String?,
    @field:Json(name = "region") val region: String?,
    @field:Json(name = "latitude") val lat: Float?,
    @field:Json(name = "longitude") val lng: Float?,
    @field:Json(name = "landing_attempts") val landingAttempts: Int,
    @field:Json(name = "landing_successes") val landingSuccesses: Int,
    @field:Json(name = "wikipedia") val wiki: String?,
    @field:Json(name = "details") val details: String?,
    @field:Json(name = "launches") val launches: List<String>,
    @field:Json(name = "id") val id: String
)