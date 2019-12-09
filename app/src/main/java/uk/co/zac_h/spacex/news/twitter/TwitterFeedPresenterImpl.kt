package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

class TwitterFeedPresenterImpl(
    private val view: TwitterFeedView,
    private val interactor: TwitterFeedInteractor
) : TwitterFeedPresenter, TwitterFeedInteractor.Callback {

    override fun getTweets() {
        view.showProgress()
        interactor.getTwitterTimeline(this)
    }

    override fun getTweets(maxId: Long) {
        view.showRecyclerLoading()
        interactor.getTwitterTimelineFromId(maxId, this)
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
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}