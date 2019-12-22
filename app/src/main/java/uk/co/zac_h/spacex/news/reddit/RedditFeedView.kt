package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel

interface RedditFeedView {

    fun updateRecycler(subredditData: SubredditModel)

    fun addPagedData(subredditData: SubredditModel)

    fun openWebLink(link: String)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeRefresh(refreshing: Boolean)

    fun showPagingProgress()

    fun hidePagingProgress()

    fun showError(error: String)

}