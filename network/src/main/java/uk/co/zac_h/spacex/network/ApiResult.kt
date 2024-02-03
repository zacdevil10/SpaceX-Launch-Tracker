package uk.co.zac_h.spacex.network

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
