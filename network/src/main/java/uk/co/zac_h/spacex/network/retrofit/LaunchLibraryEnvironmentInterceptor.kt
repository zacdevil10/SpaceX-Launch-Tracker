package uk.co.zac_h.spacex.network.retrofit

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironmentSource

/**
 * Points each Launch Library request at the current server. Retrofit's base URL is fixed when the
 * service is built, so this swaps the scheme, host and port per request instead. The path is the
 * same on every server.
 */
class LaunchLibraryEnvironmentInterceptor(
    private val environment: LaunchLibraryEnvironmentSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val target = environment.current().baseUrl.toHttpUrl()
        val url = request.url
        if (url.scheme == target.scheme && url.host == target.host && url.port == target.port) {
            return chain.proceed(request)
        }
        val rewritten = url.newBuilder()
            .scheme(target.scheme)
            .host(target.host)
            .port(target.port)
            .build()
        return chain.proceed(request.newBuilder().url(rewritten).build())
    }
}
