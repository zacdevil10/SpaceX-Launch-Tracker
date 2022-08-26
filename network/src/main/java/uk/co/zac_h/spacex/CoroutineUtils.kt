package uk.co.zac_h.spacex

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

fun <T, R> CoroutineScope.async(
    liveData: MutableLiveData<ApiResult<T>>?,
    block: suspend CoroutineScope.() -> ApiResult<R>
): Deferred<ApiResult<R>> = async {
    liveData?.value = ApiResult.Pending
    block.invoke(this)
}