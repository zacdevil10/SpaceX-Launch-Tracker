package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.rest.TwitterInterface

interface TwitterFeedInteractor {

    fun getTwitterTimeline(id: Long? = null, listener: Callback, api: TwitterInterface)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(tweets: List<TimelineTweetModel>?)
        fun onPagedSuccess(tweets: List<TimelineTweetModel>?)
        fun onError(error: String)
    }
}