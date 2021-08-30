package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.REDDIT_PARAM_ORDER_HOT
import uk.co.zac_h.spacex.datasource.remote.news.RedditPagingSource
import uk.co.zac_h.spacex.retrofit.RedditHttpClient
import uk.co.zac_h.spacex.retrofit.RedditService
import javax.inject.Inject

class RedditFeedRepository @Inject constructor(
    @RedditHttpClient private val httpService: RedditService
) {

    var order = REDDIT_PARAM_ORDER_HOT

    val redditPagingSource: RedditPagingSource
        get() = RedditPagingSource(httpService).also {
            it.order = order
        }

}