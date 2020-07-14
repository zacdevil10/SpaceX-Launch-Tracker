package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.rest.RedditInterface

interface RedditFeedContract {

    interface RedditFeedView {
        fun updateRecycler(subredditData: SubredditModel)
        fun addPagedData(subredditData: SubredditModel)
        fun openWebLink(link: String)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showPagingProgress()
        fun hidePagingProgress()
        fun showError(error: String)
    }

    interface RedditFeedPresenter {
        fun getSub(order: String, api: RedditInterface = RedditInterface.create())
        fun getNextPage(id: String, order: String, api: RedditInterface = RedditInterface.create())
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
        fun onSuccess(data: SubredditModel?)
        fun onPagedSuccess(data: SubredditModel?)
        fun onError(error: String)
    }
}