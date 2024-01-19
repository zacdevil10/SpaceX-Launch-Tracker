package uk.co.zac_h.spacex.network.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class SpaceflightNewsPaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)
