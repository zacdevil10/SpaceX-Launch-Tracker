package uk.co.zac_h.spacex.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import uk.co.zac_h.spacex.network.BuildConfig
import uk.co.zac_h.spacex.network.REDDIT_BASE_URL
import uk.co.zac_h.spacex.network.SPACEFLIGHT_NEWS_BASE_URL
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @LaunchLibraryClient
    fun providesLaunchLibraryHttpClient(): LaunchLibraryService =
        createClient(BuildConfig.URL)

    @Provides
    @SpaceflightNewsClient
    fun providesSpaceflightNewsHttpClient(): SpaceflightNewsService =
        createClient(SPACEFLIGHT_NEWS_BASE_URL)

    @Provides
    @RedditHttpClient
    fun providesRedditClient(): RedditService = createClient(REDDIT_BASE_URL)

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    private inline fun <reified T> createClient(baseUrl: String): T = Retrofit.Builder().apply {
        baseUrl(baseUrl)
        addConverterFactory(jsonConverterFactory)
        client(okHttpClient(true))
    }.build().create(T::class.java)

    private fun okHttpClient(hasLogging: Boolean = false) = OkHttpClient.Builder().apply {
        readTimeout(1, TimeUnit.MINUTES)
        connectTimeout(1, TimeUnit.MINUTES)
        if (hasLogging) addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.build()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LaunchLibraryClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpaceflightNewsClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RedditHttpClient
