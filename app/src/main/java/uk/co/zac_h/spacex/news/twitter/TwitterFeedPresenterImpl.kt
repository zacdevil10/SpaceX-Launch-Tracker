package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

class TwitterFeedPresenterImpl(
    private val view: TwitterFeedView,
    private val interactor: TwitterFeedInteractor
) : TwitterFeedPresenter, TwitterFeedInteractor.Callback {

    override fun getTweets() {
        interactor.getTwitterTimeline(this)
    }

    override fun getTweets(maxId: Long) {
        interactor.getTwitterTimelineFromId(maxId, this)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(tweets: List<TimelineTweetModel>?) {
        tweets?.let {
            view.updateRecycler(tweets)
        }
    }

    override fun onError(error: String) {

    }
}