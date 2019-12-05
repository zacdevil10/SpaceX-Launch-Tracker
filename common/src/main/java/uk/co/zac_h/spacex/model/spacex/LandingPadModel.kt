package uk.co.zac_h.spacex.model.spacex

import com.squareup.moshi.Json

data class LandingPadModel(
    @field:Json(name = "id") var id: String,
    @field:Json(name = "full_name") var nameFull: String,
    @field:Json(name = "status") var status: String,
    @field:Json(name = "location") var location: SiteLocationModel,
    @field:Json(name = "landing_type") var type: String,
    @field:Json(name = "attempted_landings") var landingAttempts: Int,
    @field:Json(name = "successful_landings") var landingSuccesses: Int,
    @field:Json(name = "wikipedia") var wiki: String,
    @field:Json(name = "details") var details: String
)