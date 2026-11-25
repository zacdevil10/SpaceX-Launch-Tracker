package uk.co.zac_h.spacex.network.retrofit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * HTTP caching for Launch Library, which allows only 15 calls per hour per IP.
 *
 * The API's own cache headers can't be relied on, so [MaxAgeInterceptor] stamps each successful
 * response with our own `max-age` per endpoint, and the OkHttp disk cache serves repeat requests
 * inside that window without a call, across app restarts. [StaleFallbackInterceptor] serves the
 * last cached response, however old, when the network fails or the API answers 429.
 */
object LaunchLibraryHttpCache {

    /** Disk cache size. Detailed launch pages are large (about 1 MB before gzip for 20). */
    const val CACHE_SIZE_BYTES = 20L * 1024 * 1024

    /** How long a response for [path] stays fresh, or null to leave it uncached. */
    fun maxAgeFor(path: String): Duration? {
        val segments = path.trim('/').split('/').let { parts ->
            // Drop the version prefix (e.g. "2.3.0") so this works for any base URL.
            if (parts.firstOrNull()?.firstOrNull()?.isDigit() == true) parts.drop(1) else parts
        }
        return when (segments.firstOrNull()) {
            "launches" -> when (segments.getOrNull(1)) {
                "upcoming" -> 1.minutes
                "previous" -> 1.hours
                else -> null
            }
            "launchers", "spacecraft" -> 1.days
            "astronauts", "agencies" -> 7.days
            else -> null
        }
    }

    /** Network interceptor: replaces the server's cache headers with our own window. */
    class MaxAgeInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            val maxAge = maxAgeFor(request.url.encodedPath)
            if (request.method != "GET" || !response.isSuccessful || maxAge == null) return response

            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Expires")
                .header("Cache-Control", "public, max-age=${maxAge.inWholeSeconds}")
                .build()
        }
    }

    /**
     * Application interceptor: when the network fails or the API answers 429, answers from the
     * disk cache instead, however stale. If nothing is cached, the original error or 429 stands.
     */
    class StaleFallbackInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            if (request.method != "GET") return chain.proceed(request)

            val response = try {
                chain.proceed(request)
            } catch (e: IOException) {
                return cached(chain) ?: throw e
            }
            if (response.code != HTTP_TOO_MANY_REQUESTS) return response

            val cached = cached(chain) ?: return response
            response.close()
            return cached
        }

        private fun cached(chain: Interceptor.Chain): Response? {
            val cacheOnly = chain.request().newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
            val response = chain.proceed(cacheOnly)
            // FORCE_CACHE answers 504 when nothing is cached.
            return if (response.isSuccessful) response else null.also { response.close() }
        }
    }

    private const val HTTP_TOO_MANY_REQUESTS = 429
}
