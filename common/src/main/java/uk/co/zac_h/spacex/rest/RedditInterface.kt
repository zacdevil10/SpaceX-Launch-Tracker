package uk.co.zac_h.spacex.rest

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.spacex.model.reddit.SubredditModel

interface RedditInterface {

    @GET("r/{subreddit}/{order}/.json?raw_json=1&count=20")
    suspend fun getRedditFeed(
        @Path("subreddit") subreddit: String,
        @Path("order") order: String = "hot",
        @Query("after") id: String? = null
    ): Response<SubredditModel>

    companion object RetrofitSetup {
        fun create(): RedditInterface = Retrofit.Builder().apply {
            baseUrl("https://reddit.com/")
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(RedditInterface::class.java)
    }
}