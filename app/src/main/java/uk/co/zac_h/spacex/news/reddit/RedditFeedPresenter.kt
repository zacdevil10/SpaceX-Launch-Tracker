package uk.co.zac_h.spacex.news.reddit

interface RedditFeedPresenter {

    fun getSub(order: String)

    fun getNextPage(id: String, order: String)

    fun cancelRequest()
}