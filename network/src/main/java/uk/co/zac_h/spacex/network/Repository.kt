package uk.co.zac_h.spacex.network

import retrofit2.HttpException
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import kotlin.time.Duration

abstract class Repository<T>(
    private val remoteDataSource: RemoteDataSource<T>,
    private val cache: Cache<T>
) {

    suspend fun fetch(
        key: String,
        cachePolicy: CachePolicy,
        maxAge: Duration = CachePolicyExpiry.DEFAULT
    ): ApiResult<T> = try {
        when (cachePolicy) {
            CachePolicy.NEVER -> fetch()
            CachePolicy.ALWAYS -> cache.get(key)?.value
            CachePolicy.CLEAR -> cache.get(key)?.value.also {
                cache.clear(key)
            }
            CachePolicy.REFRESH -> fetchAndCache(key)
            CachePolicy.EXPIRES -> cache.get(key)?.takeIf { cache.isFresh(it, maxAge) }?.value
        } ?: fetchAndCache(key)
    } catch (e: HttpException) {
        println(e.message())
        ApiResult.Failure(
            when (e.code()) {
                429 -> {
                    TooManyRequestsException(
                        e.message().filter { it.isDigit() }.let {
                            if (it.isNotEmpty()) it.toInt() else null
                        }
                    )
                }
                else -> Exception(e.message())
            }
        )
    } catch (e: Throwable) {
        ApiResult.Failure(e)
    }

    fun fetchFromCache(key: String): ApiResult<T>? = cache.get(key)?.value

    private suspend fun fetchAndCache(key: String): ApiResult<T> = fetch().also {
        cache.store(data = it, key = key)
    }

    private suspend fun fetch(): ApiResult<T> = ApiResult.Success(remoteDataSource.fetchAsync())
}
