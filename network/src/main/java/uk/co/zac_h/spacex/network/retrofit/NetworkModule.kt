package uk.co.zac_h.spacex.network.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.zac_h.spacex.network.*
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @LaunchLibraryClient
    fun providesLaunchLibraryHttpClient(): LaunchLibraryService =
        createClient(LAUNCH_LIBRARY_BASE_URL)

    @Provides
    @SpaceflightNewsClient
    fun providesSpaceflightNewsHttpClient(): SpaceflightNewsService =
        createClient(SPACEFLIGHT_NEWS_BASE_URL)

    @Provides
    @SpaceXHttpClientV4
    fun providesSpaceXHttpClientV4(): SpaceXService = createClient(SPACEX_BASE_URL_V4)

    @Provides
    @RedditHttpClient
    fun providesRedditClient(): RedditService = createClient(REDDIT_BASE_URL)

    private inline fun <reified T> createClient(baseUrl: String): T = Retrofit.Builder().apply {
        baseUrl(baseUrl)
        addConverterFactory(MoshiConverterFactory.create())
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
annotation class SpaceXHttpClientV4

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RedditHttpClient
