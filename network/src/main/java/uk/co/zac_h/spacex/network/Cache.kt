package uk.co.zac_h.spacex.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache<T> @Inject constructor() {

    private var cache: ArrayList<CacheEntry<ApiResult<T>>> = ArrayList()

    fun get(key: String): CacheEntry<ApiResult<T>>? = cache.firstOrNull { it.key == key }

    fun store(data: ApiResult<T>, key: String) {
        cache.apply {
            removeAll { it.key == key }
            add(CacheEntry(key = key, value = data))
        }
    }

    fun clear(key: String) {
        cache.removeAll { it.key == key }
    }

    fun clear() {
        cache.clear()
    }

}

object CachePolicyExpiry {
    const val LENGTH = 300000
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
    EXPIRES // Expire the cache for this key and refresh if older than expires
}
