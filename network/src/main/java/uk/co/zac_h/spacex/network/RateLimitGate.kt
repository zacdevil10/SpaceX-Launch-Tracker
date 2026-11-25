package uk.co.zac_h.spacex.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remembers when Launch Library will accept calls again after a 429, so the app stops calling it
 * until then. Every call made while throttled would fail anyway, and could extend the wait.
 */
@Singleton
class RateLimitGate @Inject constructor() {

    /** Current time in milliseconds. Replaced in tests. */
    internal var clock: () -> Long = System::currentTimeMillis

    private val _blockedUntil = MutableStateFlow<Long?>(null)

    /** Time in milliseconds when calls are allowed again, or null when not throttled. */
    val blockedUntil: StateFlow<Long?> = _blockedUntil.asStateFlow()

    /** Whole seconds until calls are allowed again (rounded up), or null when not throttled. */
    fun remainingSeconds(): Long? {
        val until = _blockedUntil.value ?: return null
        val remainingMillis = until - clock()
        if (remainingMillis <= 0) {
            _blockedUntil.compareAndSet(until, null)
            return null
        }
        return (remainingMillis + 999) / 1000
    }

    fun block(seconds: Long) {
        _blockedUntil.value = clock() + seconds * 1000
    }
}
