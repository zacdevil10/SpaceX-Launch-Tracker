package uk.co.zac_h.spacex.network

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

fun <R : Any, T : Any> Flow<PagingData<T>>.toType(
    transform: (value: T) -> R
): Flow<PagingData<R>> = map { result ->
    result.map(transform)
}

/**
 * Stored data as a screen state: the data whenever there is some, even while [refresh] is running
 * or after it failed. A failure is shown only when nothing is stored.
 */
fun <T : Any> Flow<T?>.withRefresh(refresh: Flow<ApiResult<Unit>>): Flow<ApiResult<T>> =
    combine(this, refresh) { data, result ->
        when {
            data != null -> ApiResult.Success(data)
            result is ApiResult.Failure -> result
            else -> ApiResult.Pending
        }
    }
