package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel

interface RedditFeedInteractor {

    fun getSubreddit(listener: Callback)

    fun getFromId(id: String, listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(data: SubredditModel?)
        fun onPagedSuccess(data: SubredditModel?)
        fun onError(error: String)
    }
}