package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.RedditPost
import uk.co.zac_h.spacex.retrofit.RedditService

class RedditFeedPresenterImpl(
    private val view: RedditFeedContract.RedditFeedView,
    private val interactor: RedditFeedContract.RedditFeedInteractor
) : RedditFeedContract.RedditFeedPresenter, RedditFeedContract.InteractorCallback {

    override fun getPosts(id: String?, order: String, api: RedditService) {
        id?.let {
            view.showPagingProgress()
            interactor.getSubreddit(api = api, listener = this, id = id, order = order)
        } ?: run {
            view.toggleSwipeRefresh(true)
            interactor.getSubreddit(api = api, listener = this, order = order)
        }
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: List<RedditPost>) {
        view.apply {
            toggleSwipeRefresh(false)
            updateRecycler(data)
        }
    }

    override fun onPagedSuccess(data: List<RedditPost>) {
        view.apply {
            hidePagingProgress()
            addPagedData(data)
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}