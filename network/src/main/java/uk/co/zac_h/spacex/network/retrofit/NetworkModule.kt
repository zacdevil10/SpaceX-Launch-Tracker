package uk.co.zac_h.spacex.network.retrofit

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import uk.co.zac_h.spacex.network.BuildConfig
import uk.co.zac_h.spacex.network.SPACEFLIGHT_NEWS_BASE_URL
import java.io.File
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
        @ApplicationContext context: Context
    ): LaunchLibraryService = createClient(
        baseUrl = BuildConfig.URL,
        client = okHttpClient(hasLogging = true) {
            cache(
                Cache(
                    File(context.cacheDir, "launch_library_http"),
                    LaunchLibraryHttpCache.CACHE_SIZE_BYTES
                )
            )
            addInterceptor(LaunchLibraryHttpCache.StaleFallbackInterceptor())
            addNetworkInterceptor(LaunchLibraryHttpCache.MaxAgeInterceptor())
        }
    )

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
