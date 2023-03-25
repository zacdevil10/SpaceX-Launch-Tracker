package uk.co.zac_h.spacex.network.dto.news

import com.squareup.moshi.Json

data class SpaceflightNewsPaginatedResponse<T>(
    @field:Json(name = "count") val count: Int,
    @field:Json(name = "next") val next: String?,
    @field:Json(name = "previous") val previous: String?,
    @field:Json(name = "results") val results: List<T>
)
