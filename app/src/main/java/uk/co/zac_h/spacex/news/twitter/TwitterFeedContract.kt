package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.rest.TwitterInterface

interface TwitterFeedContract {

    interface TwitterFeedView {
        fun updateRecycler(tweets: List<TimelineTweetModel>)
        fun addPagedData(tweets: List<TimelineTweetModel>)
        fun openWebLink(link: String)
        fun showScrollUp()
        fun hideScrollUp()
        fun showProgress()
        fun showPagingProgress()
        fun hideProgress()
        fun hidePagingProgress()
        fun toggleSwipeProgress(isRefreshing: Boolean)
        fun showError(error: String)
    }

    interface TwitterFeedPresenter {
        fun getTweets(api: TwitterInterface = TwitterInterface.create())
        fun getTweets(maxId: Long, api: TwitterInterface = TwitterInterface.create())
        fun toggleScrollUp(visible: Boolean)
        fun cancelRequests()
    }

    interface TwitterFeedInteractor {
        fun getTwitterTimeline(
            id: Long? = null,
            listener: InteractorCallback,
            api: TwitterInterface
        )

        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(tweets: List<TimelineTweetModel>?)
        fun onPagedSuccess(tweets: List<TimelineTweetModel>?)
        fun onError(error: String)
    }
}