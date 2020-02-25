package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.rest.RedditInterface

interface RedditFeedPresenter {

    fun getSub(order: String, api: RedditInterface = RedditInterface.create())

    fun getNextPage(id: String, order: String, api: RedditInterface = RedditInterface.create())

    fun cancelRequest()
}