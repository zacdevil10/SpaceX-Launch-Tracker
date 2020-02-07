package uk.co.zac_h.spacex.rest

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.model.twitter.OAuthKeys
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.utils.OAuthSigningInterceptor

interface TwitterInterface {

    @GET("statuses/user_timeline.json")
    suspend fun getTweets(
        @Query("screen_name") screenName: String,
        @Query("include_rts") rts: Boolean,
        @Query("trim_user") trim: Boolean,
        @Query("tweet_mode") mode: String,
        @Query("count") count: Int
    ): Response<List<TimelineTweetModel>>

    @GET("statuses/user_timeline.json")
    suspend fun getTweetsFromId(
        @Query("screen_name") screenName: String,
        @Query("include_rts") rts: Boolean,
        @Query("trim_user") trim: Boolean,
        @Query("tweet_mode") mode: String,
        @Query("count") count: Int,
        @Query("max_id") maxId: Long
    ): Response<List<TimelineTweetModel>>

    companion object RetrofitSetup {

        fun create(oAuthKeys: OAuthKeys): TwitterInterface {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.twitter.com/1.1/")
                addConverterFactory(MoshiConverterFactory.create())
                client(
                    OAuthSigningInterceptor.addKeys(oAuthKeys).build()
                )
            }.build()

            return retrofit.create(TwitterInterface::class.java)
        }
    }

}