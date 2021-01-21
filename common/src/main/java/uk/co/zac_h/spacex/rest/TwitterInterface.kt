package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.BuildConfig
import uk.co.zac_h.spacex.model.twitter.OAuthKeys
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.utils.*

interface TwitterInterface {

    @GET(TWITTER_TIMELINE)
    fun getTweets(
        @Query(TWITTER_QUERY_SCREEN_NAME) screenName: String,
        @Query(TWITTER_QUERY_INCLUDE_RTS) rts: Boolean,
        @Query(TWITTER_QUERY_TRIM_USER) trim: Boolean,
        @Query(TWITTER_QUERY_TWEET_MODE) mode: String,
        @Query(TWITTER_QUERY_COUNT) count: Int,
        @Query(TWITTER_QUERY_MAX_ID) maxId: Long? = null
    ): Call<List<TimelineTweetModel>>

    companion object RetrofitSetup {

        fun create(): TwitterInterface = Retrofit.Builder().apply {
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
        }.build().create(TwitterInterface::class.java)
    }
}