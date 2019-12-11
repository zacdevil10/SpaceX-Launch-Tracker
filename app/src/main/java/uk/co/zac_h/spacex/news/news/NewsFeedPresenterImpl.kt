package uk.co.zac_h.spacex.news.news

import uk.co.zac_h.spacex.model.news.ArticleModel

class NewsFeedPresenterImpl(
    private val view: NewsFeedView,
    private val interactor: NewsFeedInteractor
) : NewsFeedPresenter, NewsFeedInteractor.Callback {

    override fun getNews() {
        view.showProgress()
        interactor.getNews(this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(news: List<ArticleModel>?) {
        news?.let {
            view.apply {
                hideProgress()
                toggleSwipeRefresh(false)
                updateList(news)
            }
        }
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeRefresh(false)
        }
    }
}