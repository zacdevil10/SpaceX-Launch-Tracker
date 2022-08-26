package uk.co.zac_h.spacex

import uk.co.zac_h.spacex.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.QueryModel

abstract class Repository<T>(
    private val remoteDataSource: RemoteDataSource<T>,
    private val cache: Cache<T>
) {

    var location: RequestLocation = RequestLocation.REMOTE

    suspend fun fetch(
        key: String,
        query: QueryModel = QueryModel(),
        cachePolicy: CachePolicy
    ): ApiResult<T> = try {
        when (cachePolicy) {
            CachePolicy.NEVER -> fetch(query)
            CachePolicy.ALWAYS -> cache.get(key)?.value.also { location = RequestLocation.CACHE }
            CachePolicy.CLEAR -> cache.get(key)?.value.also {
                location = RequestLocation.CACHE
                cache.clear(key)
            }
            CachePolicy.REFRESH -> fetchAndCache(key, query)
            CachePolicy.EXPIRES -> cache.get(key)?.let {
                if (it.createdAt.plus(CachePolicyExpiry.LENGTH) > System.currentTimeMillis()) {
                    location = RequestLocation.CACHE
                    it.value
                } else null
            }
        } ?: fetchAndCache(key, query)
    } catch (e: Throwable) {
        ApiResult.failure(e)
    }

    fun fetchFromCache(key: String): ApiResult<T>? = cache.get(key)?.value

    private suspend fun fetchAndCache(key: String, query: QueryModel): ApiResult<T> =
        fetch(query).also { cache.store(data = it, key = key) }

    private suspend fun fetch(query: QueryModel): ApiResult<T> =
        remoteDataSource.fetchAsync(query).map { it }.also { location = RequestLocation.REMOTE }

    enum class RequestLocation {
        REMOTE, CACHE
    }
}