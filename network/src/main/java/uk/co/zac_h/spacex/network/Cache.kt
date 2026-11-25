package uk.co.zac_h.spacex.network

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Singleton
class Cache<T> @Inject constructor() {

    /** Current time in milliseconds. Replaced in tests. */
    internal var clock: () -> Long = System::currentTimeMillis

    private var cache: ArrayList<CacheEntry<ApiResult<T>>> = ArrayList()

    fun get(key: String): CacheEntry<ApiResult<T>>? = cache.firstOrNull { it.key == key }

    fun store(data: ApiResult<T>, key: String) {
        cache.apply {
            removeAll { it.key == key }
            add(CacheEntry(key = key, value = data, createdAt = clock()))
        }
    }

    fun clear(key: String) {
        cache.removeAll { it.key == key }
    }

    fun clear() {
        cache.clear()
    }

    fun isFresh(entry: CacheEntry<*>, maxAge: Duration): Boolean =
        clock() - entry.createdAt < maxAge.inWholeMilliseconds
}

/**
 * How long a cached response stays fresh for [CachePolicy.EXPIRES]. Launch Library allows only
 * 15 calls per hour per IP, so data that rarely changes is kept much longer than launches.
 */
object CachePolicyExpiry {
    val DEFAULT: Duration = 5.minutes

    /** Upcoming launches: kept very short so the next launch stays as current as possible. */
    val UPCOMING_LAUNCHES: Duration = 1.minutes

    /** The SpaceX agency (rockets, Dragon, company): changes rarely. */
    val AGENCY: Duration = 24.hours
}

data class CacheEntry<T>(
    val key: String,
    val value: T,
    val createdAt: Long = System.currentTimeMillis()
)

enum class CachePolicy {
    NEVER, //Never create a cache for the key
    ALWAYS, // Always create a cache for the key
    REFRESH, // Re-fetch the data for the key
    CLEAR, // Clear the cache for the key
    EXPIRES // Use the cache for the key if younger than maxAge, otherwise re-fetch
}
