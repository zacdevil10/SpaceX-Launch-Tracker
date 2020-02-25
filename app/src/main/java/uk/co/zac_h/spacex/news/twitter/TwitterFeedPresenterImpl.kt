package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.rest.TwitterInterface

class TwitterFeedPresenterImpl(
    private val view: TwitterFeedView,
    private val interactor: TwitterFeedInteractor
) : TwitterFeedPresenter, TwitterFeedInteractor.Callback {

    override fun getTweets(api: TwitterInterface) {
        view.showProgress()
        interactor.getTwitterTimeline(listener = this, api = api)
    }

    override fun getTweets(maxId: Long, api: TwitterInterface) {
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
                hideProgress()
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