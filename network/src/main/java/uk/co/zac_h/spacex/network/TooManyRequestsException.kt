package uk.co.zac_h.spacex.network

import retrofit2.HttpException
import uk.co.zac_h.spacex.network.retrofit.RateLimitInterceptor

/**
 * Launch Library refused the call because of its 15 calls per hour limit.
 *
 * @param time seconds until calls are allowed again, if known.
 */
class TooManyRequestsException(
    val time: Int? = null,
    private val clock: () -> Long = System::currentTimeMillis
) : Exception() {

    private val availableAtMillis: Long? = time?.let { clock() + it * 1000L }

    /** Whole seconds left until calls are allowed again (rounded up), or null if unknown. */
    fun secondsRemaining(): Long? = availableAtMillis?.let {
        ((it - clock()).coerceAtLeast(0) + 999) / 1000
    }
}

/** Turns an HTTP error into the exception the screens show, reading the wait from a 429. */
fun HttpException.toApiException(): Exception = when (code()) {
    429 -> TooManyRequestsException(
        RateLimitInterceptor.secondsFromThrottleMessage(
            runCatching { response()?.errorBody()?.string() }.getOrNull()
        )?.toInt()
    )
    else -> Exception(message())
}
