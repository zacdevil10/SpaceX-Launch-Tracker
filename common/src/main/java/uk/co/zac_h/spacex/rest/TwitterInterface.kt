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
    suspend fun getAllTweets(
        @Query("screen_name") screenName: String,
        @Query("include_rts") rts: Boolean,
        @Query("trim_user") trim: Boolean,
        @Query("tweet_mode") mode: String,
        @Query("count") count: Int
    ): Response<List<TimelineTweetModel>>

    companion object RetrofitSetup {

        private const val CONSUMER_KEY = "ISEOHNSG90k2ovXG3NpzIISQR"
        private const val CONSUMER_SECRET = "CMBENg6I4azmMbBoIGN2o3ejJ7O3FDdhYg9mWh0CTGMAXFcWEl"
        private const val ACCESS_TOKEN = "1009867578221711361-FVwuXIyNNLmfN12dV662BPJNF0R0VS"
        private const val TOKEN_SECRET = "4E7sZa6Jl8DwFHPLAdPkClC3x7gE4iNGpNEL9RknxOHOK"

        fun create(): TwitterInterface {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.twitter.com/1.1/")
                addConverterFactory(MoshiConverterFactory.create())
                client(
                    OAuthSigningInterceptor.addKeys(
                        OAuthKeys(
                            CONSUMER_KEY,
                            CONSUMER_SECRET,
                            ACCESS_TOKEN,
                            TOKEN_SECRET
                        )
                    ).build()
                )
            }.build()

            return retrofit.create(TwitterInterface::class.java)
        }
    }

}