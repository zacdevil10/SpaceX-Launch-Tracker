package uk.co.zac_h.spacex.network

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun <R : Any, T : Any> Flow<PagingData<T>>.toType(
    transform: (value: T) -> R
): Flow<PagingData<R>> = map { result ->
    result.map(transform)
}

fun <T : Any> MutableStateFlow<ApiResult<T>>.get(
    scope: CoroutineScope,
    block: suspend () -> ApiResult<T>
) {
    value = ApiResult.Pending
    scope.launch { value = block() }
}
