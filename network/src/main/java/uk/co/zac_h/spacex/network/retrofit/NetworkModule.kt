package uk.co.zac_h.spacex.network.retrofit

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.zac_h.spacex.network.*
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun apiTokenManager(
        @ApplicationContext context: Context
    ): ApiTokenManager = ApiTokenManager(context)

    @Provides
    @Singleton
    fun authInterceptor(
        apiTokenManager: ApiTokenManager
    ): AuthInterceptor = AuthInterceptor(apiTokenManager)

    @Provides
    @LaunchLibraryClient
    fun providesLaunchLibraryHttpClient(authInterceptor: AuthInterceptor): LaunchLibraryService =
        createClient(LAUNCH_LIBRARY_BASE_URL, authInterceptor)

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

    private inline fun <reified T> createClient(
        baseUrl: String,
        authInterceptor: AuthInterceptor? = null
    ): T = Retrofit.Builder().apply {
        baseUrl(baseUrl)
        addConverterFactory(MoshiConverterFactory.create())
        client(okHttpClient(true, authInterceptor))
    }.build().create(T::class.java)

    private fun okHttpClient(
        hasLogging: Boolean = false,
        authInterceptor: AuthInterceptor?
    ) = OkHttpClient.Builder().apply {
        readTimeout(1, TimeUnit.MINUTES)
        connectTimeout(1, TimeUnit.MINUTES)
        if (hasLogging) addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        authInterceptor?.let { addInterceptor(authInterceptor) }
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
