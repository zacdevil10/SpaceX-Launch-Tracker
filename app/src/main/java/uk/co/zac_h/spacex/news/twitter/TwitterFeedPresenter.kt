package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.rest.TwitterInterface

interface TwitterFeedPresenter {

    fun getTweets(api: TwitterInterface = TwitterInterface.create())

    fun getTweets(maxId: Long, api: TwitterInterface = TwitterInterface.create())

    fun toggleScrollUp(visible: Boolean)

    fun cancelRequests()
}