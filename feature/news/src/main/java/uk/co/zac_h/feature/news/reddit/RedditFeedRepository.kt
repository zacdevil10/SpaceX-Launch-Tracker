package uk.co.zac_h.feature.news.reddit

import uk.co.zac_h.spacex.network.datasource.remote.news.RedditPagingSource
import uk.co.zac_h.spacex.network.retrofit.RedditHttpClient
import uk.co.zac_h.spacex.network.retrofit.RedditService
import javax.inject.Inject

class RedditFeedRepository @Inject constructor(
    @RedditHttpClient private val httpService: RedditService
) {

    val redditPagingSource: RedditPagingSource
        get() = RedditPagingSource(httpService)
}
