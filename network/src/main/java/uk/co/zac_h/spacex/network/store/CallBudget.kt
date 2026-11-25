package uk.co.zac_h.spacex.network.store

import okhttp3.Interceptor
import okhttp3.Response
import java.util.ArrayDeque
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours

/**
 * Counts Launch Library calls in the last hour, so automatic refreshes can stop before they use up
 * calls the user needs for opening screens. Launch Library allows 15 calls per hour per IP.
 *
 * Installed as a network interceptor, so it counts only calls that reach the network. The count
 * is per process: after a restart it starts from zero, and the API's own 429 is the backstop.
 */
@Singleton
class CallBudget @Inject constructor(private val clock: Clock) : Interceptor {

    private val calls = ArrayDeque<Long>()

    @Synchronized
    fun record() {
        calls.addLast(clock.now())
    }

    /** Calls left in the current hour, by our own count. */
    @Synchronized
    fun remaining(): Int {
        val windowStart = clock.now() - WINDOW_MILLIS
        while (calls.isNotEmpty() && calls.first() <= windowStart) calls.removeFirst()
        return (LIMIT - calls.size).coerceAtLeast(0)
    }

    /** Whether an automatic refresh may spend a call without touching the reserve for screens. */
    fun allowsAutomaticRefresh(): Boolean = remaining() > RESERVED_FOR_SCREENS

    override fun intercept(chain: Interceptor.Chain): Response {
        record()
        return chain.proceed(chain.request())
    }

    companion object {
        const val LIMIT = 15
        const val RESERVED_FOR_SCREENS = 4
        private val WINDOW_MILLIS = 1.hours.inWholeMilliseconds
    }
}
