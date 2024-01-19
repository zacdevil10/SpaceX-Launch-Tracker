package uk.co.zac_h.spacex.network.dto.spacex

import kotlinx.serialization.Serializable
import uk.co.zac_h.spacex.network.ApiResult

@Serializable
data class LaunchLibraryPaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>,
    val detail: String?
)

fun <R, T> ApiResult<LaunchLibraryPaginatedResponse<T>>.mapResults(
    transform: (value: T) -> R
) = map {
    it.mapResponse(transform)
}

internal fun <R, T> LaunchLibraryPaginatedResponse<T>.mapResponse(
    transform: (value: T) -> R
) = results.map(transform)
