package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel

class RedditFeedPresenterImpl(
    private val view: RedditFeedView,
    private val interactor: RedditFeedInteractor
) : RedditFeedPresenter, RedditFeedInteractor.Callback {

    override fun getSub(order: String) {
        view.showProgress()
        interactor.getSubreddit(listener = this, order = order)
    }

    override fun getNextPage(id: String, order: String) {
        view.showPagingProgress()
        interactor.getSubreddit(listener = this, id = id, order = order)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(data: SubredditModel?) {
        view.apply {
            hideProgress()
            toggleSwipeRefresh(false)
        }
        data?.let { view.updateRecycler(it) }
    }

    override fun onPagedSuccess(data: SubredditModel?) {
        view.hidePagingProgress()
        data?.let { view.addPagedData(data) }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}