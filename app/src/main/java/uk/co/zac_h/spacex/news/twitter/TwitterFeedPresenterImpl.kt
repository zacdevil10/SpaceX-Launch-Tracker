package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.dto.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.retrofit.TwitterService

class TwitterFeedPresenterImpl(
    private val view: TwitterFeedContract.TwitterFeedView,
    private val interactor: TwitterFeedContract.TwitterFeedInteractor
) : TwitterFeedContract.TwitterFeedPresenter, TwitterFeedContract.InteractorCallback {

    override fun getTweets(api: TwitterService) {
        view.toggleSwipeProgress(true)
        interactor.getTwitterTimeline(listener = this, api = api)
    }

    override fun getTweets(maxId: Long, api: TwitterService) {
        view.showPagingProgress()
        interactor.getTwitterTimeline(maxId, this, api)
    }

    override fun toggleScrollUp(visible: Boolean) {
        if (visible) {
            view.showScrollUp()
        } else {
            view.hideScrollUp()
        }
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(tweets: List<TimelineTweetModel>?) {
        tweets?.let {
            view.apply {
                updateRecycler(tweets)
                toggleSwipeProgress(false)
            }
        }
    }

    override fun onPagedSuccess(tweets: List<TimelineTweetModel>?) {
        tweets?.let {
            view.addPagedData(it)
            view.hidePagingProgress()
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }
}