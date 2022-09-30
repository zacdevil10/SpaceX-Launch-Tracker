package uk.co.zac_h.spacex.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.dto.twitter.TimelineTweetModel

interface TwitterService {

    @GET(TWITTER_TIMELINE)
    suspend fun getTweets(
        @Query(TWITTER_QUERY_SCREEN_NAME) screenName: String,
        @Query(TWITTER_QUERY_INCLUDE_RTS) rts: Boolean,
        @Query(TWITTER_QUERY_TRIM_USER) trim: Boolean,
        @Query(TWITTER_QUERY_TWEET_MODE) mode: String,
        @Query(TWITTER_QUERY_COUNT) count: Int,
        @Query(TWITTER_QUERY_MAX_ID) maxId: Long? = null
    ): Response<List<TimelineTweetModel>>
}
