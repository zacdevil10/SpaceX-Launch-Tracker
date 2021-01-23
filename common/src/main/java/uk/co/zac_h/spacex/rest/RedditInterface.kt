package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.utils.*

interface RedditInterface {

    @GET(REDDIT_SUBREDDIT)
    fun getRedditFeed(
        @Path(REDDIT_PARAM_SUBREDDIT) subreddit: String,
        @Path(REDDIT_PARAM_ORDER) order: String = REDDIT_PARAM_ORDER_HOT,
        @Query(REDDIT_QUERY_AFTER) id: String? = null
    ): Call<SubredditModel>

    companion object RetrofitSetup {
        fun create(): RedditInterface = Retrofit.Builder().apply {
            baseUrl(REDDIT_BASE_URL)
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(RedditInterface::class.java)
    }
}