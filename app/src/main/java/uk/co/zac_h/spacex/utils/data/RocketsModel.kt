package uk.co.zac_h.spacex.utils.data

import com.squareup.moshi.Json

data class RocketsModel(
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "stages") var stages: Int,
    @field:Json(name = "boosters") var boosters: Int,
    @field:Json(name = "cost_per_launch") var costPerLaunch: Int,
    @field:Json(name = "success_rate_pct") var successRate: Int,
    @field:Json(name = "first_flight") var firstFlight: String,
    @field:Json(name = "country") var country: String,
    @field:Json(name = "company") var company: String
)