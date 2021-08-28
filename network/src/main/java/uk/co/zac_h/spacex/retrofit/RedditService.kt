package uk.co.zac_h.spacex.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.model.reddit.SubredditModel

interface RedditService {

    @GET(REDDIT_SUBREDDIT)
    fun getRedditFeed(
        @Path(REDDIT_PARAM_SUBREDDIT) subreddit: String,
        @Path(REDDIT_PARAM_ORDER) order: String = REDDIT_PARAM_ORDER_HOT,
        @Query(REDDIT_QUERY_AFTER) id: String? = null
    ): Call<SubredditModel>

}