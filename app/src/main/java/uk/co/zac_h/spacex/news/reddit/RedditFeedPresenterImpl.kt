package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel

class RedditFeedPresenterImpl(
    private val view: RedditFeedView,
    private val interactor: RedditFeedInteractor
) : RedditFeedPresenter, RedditFeedInteractor.Callback {

    override fun getSub() {
        interactor.getSubreddit(this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: SubredditModel?) {
        data?.let { view.updateRecycler(it) }
    }

    override fun onError(error: String) {

    }
}