package uk.co.zac_h.spacex.network

import kotlinx.coroutines.Deferred
import retrofit2.Response

sealed class ApiResult<out R> {

    object Pending : ApiResult<Nothing>()
    data class Success<out T>(internal val result: T) : ApiResult<T>()
    data class Failure(val exception: Throwable) : ApiResult<Nothing>()

    fun <T> map(transform: (value: R) -> T): ApiResult<T> = when (this) {
        is Pending -> this
        is Success -> Success(this.result.let(transform))
        is Failure -> this
    }

    val data: R?
        get() = if (this is Success) this.result else null
}

suspend fun <R, T> Deferred<Response<T>>.map(transform: (value: T) -> R): ApiResult<R> = try {
    this.await().body()?.let {
        ApiResult.Success(it.let(transform))
    } ?: ApiResult.Failure(IllegalArgumentException("No body found"))
} catch (e: Throwable) {
    ApiResult.Failure(e)
}

fun <R, T> Response<T>.map(transform: (value: T) -> R): ApiResult<R> = try {
    this.body()?.let {
        ApiResult.Success(it.let(transform))
    } ?: ApiResult.Failure(IllegalArgumentException("No body found"))
} catch (e: Throwable) {
    ApiResult.Failure(e)
}
