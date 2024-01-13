package uk.co.zac_h.spacex.network

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse

fun <T> apiFlow(block: suspend () -> ApiResult<T>): Flow<ApiResult<T>> = flow {
    emit(block())
}

fun <R, T> Flow<ApiResult<T>>.toType(
    transform: (value: T) -> R
): Flow<ApiResult<R>> = map { result ->
    result.map(transform)
}

@JvmName(name = "toLaunchLibraryPaginatedResponseType")
fun <R, T> Flow<ApiResult<LaunchLibraryPaginatedResponse<T>>>.toType(
    transform: (value: T) -> R
): Flow<ApiResult<List<R>>> = map { result ->
    result.map {
        it.results.map(transform)
    }
}

@JvmName(name = "toPagingDataType")
fun <R : Any, T : Any> Flow<PagingData<T>>.toType(
    transform: (value: T) -> R
): Flow<PagingData<R>> = map { result ->
    result.map(transform)
}
