package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.datasource.remote.news.TwitterPagingSource
import uk.co.zac_h.spacex.retrofit.TwitterHttpClient
import uk.co.zac_h.spacex.retrofit.TwitterService
import javax.inject.Inject

class TwitterFeedRepository @Inject constructor(
    @TwitterHttpClient private val httpService: TwitterService
) {

    val twitterPagingSource: TwitterPagingSource
        get() = TwitterPagingSource(httpService)

}