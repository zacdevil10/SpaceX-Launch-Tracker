package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.datasource.news.RedditPagingSource
import uk.co.zac_h.spacex.retrofit.RedditHttpClient
import uk.co.zac_h.spacex.retrofit.RedditService
import javax.inject.Inject

class RedditFeedRepository @Inject constructor(
    @RedditHttpClient httpService: RedditService
) {

    val redditPagingSource = RedditPagingSource(httpService)

    fun setOrder(order: String) {
        redditPagingSource.order = order
    }

}