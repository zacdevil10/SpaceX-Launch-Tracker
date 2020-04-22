package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.rest.RedditInterface

class RedditFeedPresenterImpl(
    private val view: RedditFeedContract.RedditFeedView,
    private val interactor: RedditFeedContract.RedditFeedInteractor
) : RedditFeedContract.RedditFeedPresenter, RedditFeedContract.InteractorCallback {

    override fun getSub(order: String, api: RedditInterface) {
        view.showProgress()
        interactor.getSubreddit(api = api, listener = this, order = order)
    }

    override fun getNextPage(id: String, order: String, api: RedditInterface) {
        view.showPagingProgress()
        interactor.getSubreddit(api = api, listener = this, id = id, order = order)
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