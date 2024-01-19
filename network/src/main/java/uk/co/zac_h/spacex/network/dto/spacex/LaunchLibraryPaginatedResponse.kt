package uk.co.zac_h.spacex.network.dto.spacex

data class LaunchLibraryPaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>,
    val detail: String?
)
