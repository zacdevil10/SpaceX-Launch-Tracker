package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.dto.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.retrofit.NetworkModule
import uk.co.zac_h.spacex.retrofit.TwitterService

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
        fun getTweets(api: TwitterService = NetworkModule.providesTwitterClient())
        fun getTweets(maxId: Long, api: TwitterService = NetworkModule.providesTwitterClient())
        fun toggleScrollUp(visible: Boolean)
        fun cancelRequests()
    }

    interface TwitterFeedInteractor {
        fun getTwitterTimeline(
            id: Long? = null,
            listener: InteractorCallback,
            api: TwitterService
        )

        fun cancelAllRequests(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(tweets: List<TimelineTweetModel>?)
        fun onPagedSuccess(tweets: List<TimelineTweetModel>?)
        fun onError(error: String)
    }
}