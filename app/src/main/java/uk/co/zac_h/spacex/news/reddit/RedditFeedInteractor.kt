package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.rest.RedditInterface

interface RedditFeedInteractor {

    fun getSubreddit(api: RedditInterface, listener: Callback, order: String, id: String? = null)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(data: SubredditModel?)
        fun onPagedSuccess(data: SubredditModel?)
        fun onError(error: String)
    }
}