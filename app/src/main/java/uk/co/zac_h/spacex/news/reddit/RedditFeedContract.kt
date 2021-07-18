package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.RedditPost
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.rest.RedditInterface

interface RedditFeedContract {

    interface RedditFeedView {
        fun updateRecycler(subredditData: List<RedditPost>)
        fun addPagedData(subredditData: List<RedditPost>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showPagingProgress()
        fun hidePagingProgress()
        fun showError(error: String)
    }

    interface RedditFeedPresenter {
        fun getPosts(id: String? = null, order: String, api: RedditInterface = RedditInterface.create())
        fun cancelRequest()
    }

    interface RedditFeedInteractor {
        fun getSubreddit(
            api: RedditInterface,
            listener: InteractorCallback,
            order: String,
            id: String? = null
        )

        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(data: List<RedditPost>)
        fun onPagedSuccess(data: List<RedditPost>)
        fun onError(error: String)
    }
}