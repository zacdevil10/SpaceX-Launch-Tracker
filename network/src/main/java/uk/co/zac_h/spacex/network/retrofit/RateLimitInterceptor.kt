package uk.co.zac_h.spacex.network.retrofit

import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import uk.co.zac_h.spacex.network.RateLimitGate

/**
 * Stops Launch Library calls while the API is throttling us.
 *
 * - While [gate] is closed, answers every request with a local 429 instead of calling the API.
 * - On a real 429, reads the wait from the response body, or asks [lookUpWaitSeconds] (the free
 *   `api-throttle/` endpoint) when the body has no number, and closes [gate] for that long.
 *
 * Every 429 that leaves this interceptor carries the wait in one known format,
 * "Expected available in N seconds.", which [secondsFromThrottleMessage] reads back.
 */
class RateLimitInterceptor(
    private val gate: RateLimitGate,
    private val lookUpWaitSeconds: () -> Long?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        gate.remainingSeconds()?.let { seconds ->
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(HTTP_TOO_MANY_REQUESTS)
                .message("Too Many Requests")
                .body(throttleMessage(seconds).toResponseBody(JSON))
                .build()
        }

        val response = chain.proceed(request)
        if (response.code != HTTP_TOO_MANY_REQUESTS) return response

        val fromBody = secondsFromThrottleMessage(response.peekBody(MAX_PEEK_BYTES).string())
        val seconds = fromBody ?: runCatching(lookUpWaitSeconds).getOrNull() ?: DEFAULT_WAIT_SECONDS
        gate.block(seconds)

        return response.newBuilder()
            .body(throttleMessage(seconds).toResponseBody(JSON))
            .build()
            .also { response.close() }
    }

    companion object {
        private const val HTTP_TOO_MANY_REQUESTS = 429
        private const val MAX_PEEK_BYTES = 16L * 1024

        /** Used when neither the 429 body nor `api-throttle/` gives a wait. */
        internal const val DEFAULT_WAIT_SECONDS = 5L * 60

        private val JSON = "application/json".toMediaType()
        private val SECONDS = Regex("""(\d+)\s*seconds?""")

        internal fun throttleMessage(seconds: Long) =
            """{"detail":"Request was throttled. Expected available in $seconds seconds."}"""

        /** Reads N from "... Expected available in N seconds." or null if there is no such number. */
        fun secondsFromThrottleMessage(body: String?): Long? =
            body?.let { SECONDS.find(it)?.groupValues?.get(1)?.toLongOrNull() }

        /** Reads `next_use_secs` from an `api-throttle/` response. */
        internal fun secondsFromThrottleEndpoint(body: String): Long? =
            runCatching {
                networkJson.parseToJsonElement(body).jsonArray.firstOrNull()
                    ?.jsonObject?.get("next_use_secs")?.jsonPrimitive?.longOrNull
                    ?.takeIf { it > 0 }
            }.getOrNull()
    }
}
