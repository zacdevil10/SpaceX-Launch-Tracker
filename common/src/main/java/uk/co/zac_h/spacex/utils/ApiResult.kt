package uk.co.zac_h.spacex.utils

import kotlinx.coroutines.Deferred
import retrofit2.Response

data class ApiResult<out T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {

    companion object {
        fun <T> pending(): ApiResult<T> = ApiResult(status = Status.PENDING)

        fun <T> success(data: T?): ApiResult<T> = ApiResult(status = Status.SUCCESS, data = data)

        fun <T> failure(error: Throwable): ApiResult<T> =
            ApiResult(status = Status.FAILURE, error = error)
    }

    enum class Status {
        PENDING,
        SUCCESS,
        FAILURE
    }

}

suspend fun <R, T> Deferred<Response<T>>.map(transform: (value: T) -> R): ApiResult<R> = try {
    ApiResult.success(this.await().body()?.let(transform))
} catch (e: Throwable) {
    ApiResult.failure(e)
}