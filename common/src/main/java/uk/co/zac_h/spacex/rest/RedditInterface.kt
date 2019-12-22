package uk.co.zac_h.spacex.rest

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.spacex.model.reddit.SubredditModel

interface RedditInterface {

    @GET("r/{subreddit}/.json")
    suspend fun getRedditFeed(@Path("subreddit") subreddit: String, @Query("after") id: String? = null): Response<SubredditModel>

    companion object RetrofitSetup {
        fun create(): RedditInterface {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://reddit.com/")
                addConverterFactory(MoshiConverterFactory.create())
            }.build()

            return retrofit.create(RedditInterface::class.java)
        }
    }
}