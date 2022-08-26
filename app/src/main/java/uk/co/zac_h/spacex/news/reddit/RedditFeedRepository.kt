package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.datasource.remote.news.RedditPagingSource
import uk.co.zac_h.spacex.retrofit.RedditHttpClient
import uk.co.zac_h.spacex.retrofit.RedditService
import javax.inject.Inject

class RedditFeedRepository @Inject constructor(
    @RedditHttpClient private val httpService: RedditService
) {

    val redditPagingSource: RedditPagingSource
        get() = RedditPagingSource(httpService)
}