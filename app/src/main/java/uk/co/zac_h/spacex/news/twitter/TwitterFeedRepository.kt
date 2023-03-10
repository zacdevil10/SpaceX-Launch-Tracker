package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.network.datasource.remote.news.TwitterPagingSource
import uk.co.zac_h.spacex.network.retrofit.TwitterHttpClient
import uk.co.zac_h.spacex.network.retrofit.TwitterService
import javax.inject.Inject

class TwitterFeedRepository @Inject constructor(
    @TwitterHttpClient private val httpService: TwitterService
) {

    val twitterPagingSource: TwitterPagingSource
        get() = TwitterPagingSource(httpService)
}
