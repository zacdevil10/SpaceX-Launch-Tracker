package uk.co.zac_h.spacex.network.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.spacex.network.*
import uk.co.zac_h.spacex.network.dto.reddit.SubredditModel

interface RedditService {

    @GET(REDDIT_SUBREDDIT)
    suspend fun getRedditFeed(
        @Path(REDDIT_PARAM_SUBREDDIT) subreddit: String,
        @Path(REDDIT_PARAM_ORDER) order: String = REDDIT_PARAM_ORDER_HOT,
        @Query(REDDIT_PARAM_LIMIT) limit: Int,
        @Query(REDDIT_QUERY_AFTER) id: String? = null
    ): Response<SubredditModel>
}
