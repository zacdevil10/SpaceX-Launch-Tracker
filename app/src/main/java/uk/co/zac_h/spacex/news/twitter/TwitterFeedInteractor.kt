package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

interface TwitterFeedInteractor {

    fun getTwitterTimeline(listener: Callback)

    fun getTwitterTimelineFromId(id: Long, listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(tweets: List<TimelineTweetModel>?)
        fun onError(error: String)
    }
}