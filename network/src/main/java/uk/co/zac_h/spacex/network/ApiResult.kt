package uk.co.zac_h.spacex.network

import retrofit2.Response

sealed class ApiResult<out R> {

    data object Pending : ApiResult<Nothing>()
    data class Success<out T>(val result: T) : ApiResult<T>()
    data class Failure(val exception: Throwable) : ApiResult<Nothing>()

    fun <T> map(transform: (value: R) -> T): ApiResult<T> = when (this) {
        is Pending -> this
        is Success -> Success(this.result.let(transform))
        is Failure -> this
    }
}

fun <R, T> Response<T>.map(transform: (value: T) -> R): ApiResult<R> = try {
    if (isSuccessful) {
        body()?.let {
            ApiResult.Success(it.let(transform))
        } ?: ApiResult.Failure(IllegalArgumentException("No body found"))
    } else {
        ApiResult.Failure(Exception(errorBody()?.string()))
    }
} catch (e: Throwable) {
    ApiResult.Failure(e)
}
