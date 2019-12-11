package uk.co.zac_h.spacex.news.news

import uk.co.zac_h.spacex.model.news.ArticleModel

interface NewsFeedInteractor {

    fun getNews(listener: Callback)

    fun cancelAllRequests()

    interface Callback {
        fun onSuccess(news: List<ArticleModel>?)
        fun onError(error: String)
    }
}