package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.OAuthKeys
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

interface TwitterFeedInteractor {

    fun getTwitterTimeline(listener: Callback, oAuthKeys: OAuthKeys)

    fun getTwitterTimelineFromId(id: Long, listener: Callback, oAuthKeys: OAuthKeys)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(tweets: List<TimelineTweetModel>?)
        fun onPagedSuccess(tweets: List<TimelineTweetModel>?)
        fun onError(error: String)
    }
}