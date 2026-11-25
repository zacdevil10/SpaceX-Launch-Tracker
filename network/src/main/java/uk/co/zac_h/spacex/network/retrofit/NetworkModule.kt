package uk.co.zac_h.spacex.network.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import uk.co.zac_h.spacex.network.API_THROTTLE
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironment
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironmentSource
import uk.co.zac_h.spacex.network.RateLimitGate
import uk.co.zac_h.spacex.network.SPACEFLIGHT_NEWS_BASE_URL
import uk.co.zac_h.spacex.network.store.CallBudget
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @LaunchLibraryClient
    fun providesLaunchLibraryHttpClient(
        rateLimitGate: RateLimitGate,
        callBudget: CallBudget,
        environment: LaunchLibraryEnvironmentSource
    ): LaunchLibraryService {
        // A plain client for the free api-throttle/ endpoint, outside the gate.
        val throttleClient = okHttpClient()
        return createClient(
            // Rewritten per request to the current server by LaunchLibraryEnvironmentInterceptor.
            baseUrl = LaunchLibraryEnvironment.PRODUCTION.baseUrl,
            client = okHttpClient(hasLogging = true) {
                addInterceptor(LaunchLibraryEnvironmentInterceptor(environment))
                addInterceptor(
                    RateLimitInterceptor(rateLimitGate) {
                        val request = Request.Builder().url(environment.current().baseUrl + API_THROTTLE).build()
                        throttleClient.newCall(request).execute().use { response ->
                            if (response.isSuccessful) {
                                RateLimitInterceptor.secondsFromThrottleEndpoint(response.body.string())
                            } else null
                        }
                    }
                )
                // Counts calls that reach the network, for the automatic refresh budget.
                addNetworkInterceptor(callBudget)
            }
        )
    }

    @Provides
    @SpaceflightNewsClient
    fun providesSpaceflightNewsHttpClient(): SpaceflightNewsService =
        createClient(SPACEFLIGHT_NEWS_BASE_URL, okHttpClient(hasLogging = true))

    private val jsonConverterFactory = networkJson.asConverterFactory("application/json".toMediaType())

    private inline fun <reified T> createClient(baseUrl: String, client: OkHttpClient): T =
        Retrofit.Builder().apply {
            baseUrl(baseUrl)
            addConverterFactory(jsonConverterFactory)
            client(client)
        }.build().create(T::class.java)

    private fun okHttpClient(
        hasLogging: Boolean = false,
        configure: OkHttpClient.Builder.() -> Unit = {}
    ) = OkHttpClient.Builder().apply {
        readTimeout(1.minutes)
        connectTimeout(1.minutes)
        configure()
        if (hasLogging) addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.build()
}

/**
 * Shared by every API client. `ignoreUnknownKeys` lets the APIs add fields without breaking us, and
 * `explicitNulls = false` treats missing nullable fields as null.
 */
internal val networkJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LaunchLibraryClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpaceflightNewsClient
