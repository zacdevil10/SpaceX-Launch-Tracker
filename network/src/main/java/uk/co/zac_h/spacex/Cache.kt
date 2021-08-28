package uk.co.zac_h.spacex

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache<T> @Inject constructor() {

    private var data: ArrayList<CacheEntry<ApiResult<T>>> = ArrayList()

    fun get(key: String): CacheEntry<ApiResult<T>>? = data.firstOrNull { it.key == key }

    fun store(data: ApiResult<T>, key: String) {
        this.data.removeAll { it.key == key }
        this.data.add(CacheEntry(key = key, value = data))
    }

    fun clear(key: String) {
        data.removeAll { it.key == key }
    }

    fun clear() {
        data.clear()
    }

}

object CachePolicyExpiry {
    const val LENGTH = 5000
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