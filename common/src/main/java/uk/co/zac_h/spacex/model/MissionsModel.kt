package uk.co.zac_h.spacex.model

import com.squareup.moshi.Json

data class MissionsModel(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "flight") val flightNumber: Int?
)