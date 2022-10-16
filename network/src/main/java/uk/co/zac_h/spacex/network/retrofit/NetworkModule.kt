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
        createClient(MOCK_LAUNCH_LIBRARY_BASE_URL)

    @Provides
    @SpaceXHttpClientV4
    fun providesSpaceXHttpClientV4(): SpaceXService = createClient(SPACEX_BASE_URL_V4)

    @Provides
    @SpaceXHttpClientV5
    fun providesSpaceXHttpClientV5(): SpaceXService = createClient(SPACEX_BASE_URL_V5)

    @Provides
    @RedditHttpClient
    fun providesRedditClient(): RedditService = createClient(REDDIT_BASE_URL)

    @Provides
    @TwitterHttpClient
    fun providesTwitterClient(): TwitterService = Retrofit.Builder().apply {
        baseUrl(TWITTER_BASE_URL)
        addConverterFactory(MoshiConverterFactory.create())
        client(
            OAuthSigningInterceptor.addKeys(
                OAuthKeys(
                    BuildConfig.CONSUMER_KEY,
                    BuildConfig.CONSUMER_SECRET,
                    BuildConfig.ACCESS_TOKEN,
                    BuildConfig.TOKEN_SECRET
                )
            ).build()
        )
    }.build().create(TwitterService::class.java)

    private inline fun <reified T> createClient(baseUrl: String): T = Retrofit.Builder().apply {
        baseUrl(baseUrl)
        addConverterFactory(MoshiConverterFactory.create())
        client(okHttpClient())
        client(loggingClient())
    }.build().create(T::class.java)

    private fun loggingClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun okHttpClient() = OkHttpClient.Builder()
        .readTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .build()

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LaunchLibraryClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpaceXHttpClientV4

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpaceXHttpClientV5

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RedditHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitterHttpClient
