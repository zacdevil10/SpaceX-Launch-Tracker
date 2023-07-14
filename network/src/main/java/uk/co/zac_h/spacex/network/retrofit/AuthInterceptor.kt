package uk.co.zac_h.spacex.network.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import uk.co.zac_h.spacex.network.ApiTokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val apiTokenManager: ApiTokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            if (apiTokenManager.hasSubscribed) {
                addHeader(
                    "Authorization",
                    "Token ${apiTokenManager.token}"
                )
            }
        }.build()

        return chain.proceed(request)
    }
}