package uk.co.zac_h.spacex.news.twitter

interface TwitterFeedPresenter {

    fun getTweets()

    fun getTweets(maxId: Long)

    fun toggleScrollUp(visible: Boolean)

    fun cancelRequests()
}