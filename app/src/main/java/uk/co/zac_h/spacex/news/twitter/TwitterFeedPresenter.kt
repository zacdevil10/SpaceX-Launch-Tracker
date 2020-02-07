package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.OAuthKeys

interface TwitterFeedPresenter {

    fun getTweets(oAuthKeys: OAuthKeys)

    fun getTweets(maxId: Long, oAuthKeys: OAuthKeys)

    fun toggleScrollUp(visible: Boolean)

    fun cancelRequests()
}