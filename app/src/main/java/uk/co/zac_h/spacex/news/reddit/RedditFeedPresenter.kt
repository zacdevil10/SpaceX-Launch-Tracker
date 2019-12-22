package uk.co.zac_h.spacex.news.reddit

interface RedditFeedPresenter {

    fun getSub()

    fun getNextPage(id: String)

    fun cancelRequest()
}