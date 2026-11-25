package uk.co.zac_h.spacex.network.dto.spacex

import kotlinx.serialization.Serializable
import uk.co.zac_h.spacex.network.ApiResult

/**
 * Launch Library's paginated list envelope. Hand-written rather than generated so it can be
 * generic over the concrete `mode` model each endpoint returns.
 */
@Serializable
data class LaunchLibraryPaginatedResponse<T>(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T>
)

fun <R, T> ApiResult<LaunchLibraryPaginatedResponse<T>>.mapResults(
    transform: (value: T) -> R
) = map {
    it.mapResponse(transform)
}

internal fun <R, T> LaunchLibraryPaginatedResponse<T>.mapResponse(
    transform: (value: T) -> R
) = results.map(transform)
