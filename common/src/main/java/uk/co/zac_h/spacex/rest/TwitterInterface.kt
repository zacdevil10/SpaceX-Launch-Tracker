package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.BuildConfig
import uk.co.zac_h.spacex.model.twitter.OAuthKeys
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.utils.OAuthSigningInterceptor

interface TwitterInterface {

    @GET("statuses/user_timeline.json")
    fun getTweets(
        @Query("screen_name") screenName: String,
        @Query("include_rts") rts: Boolean,
        @Query("trim_user") trim: Boolean,
        @Query("tweet_mode") mode: String,
        @Query("count") count: Int,
        @Query("max_id") maxId: Long? = null
    ): Call<List<TimelineTweetModel>>

    companion object RetrofitSetup {

        fun create(): TwitterInterface = Retrofit.Builder().apply {
            baseUrl("https://api.twitter.com/1.1/")
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