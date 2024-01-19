package uk.co.zac_h.spacex.network

import retrofit2.HttpException

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

fun <R, T> T.toApiResource(transform: (value: T) -> R): ApiResult<R> = try {
    ApiResult.Success(let(transform))
} catch (e: HttpException) {
    ApiResult.Failure(
        when (e.code()) {
            429 -> TooManyRequestsException(
                e.message().filter { it.isDigit() }.toInt()
            )
            else -> Exception(e.message())
        }
    )
} catch (e: Throwable) {
    ApiResult.Failure(e)
}
