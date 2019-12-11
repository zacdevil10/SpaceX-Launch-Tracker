package uk.co.zac_h.spacex.news.news

import uk.co.zac_h.spacex.model.news.ArticleModel

interface NewsFeedView {

    fun updateList(articles: List<ArticleModel>)

    fun toggleSwipeRefresh(isRefreshing: Boolean)

    fun openWebLink(link: String)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)
}