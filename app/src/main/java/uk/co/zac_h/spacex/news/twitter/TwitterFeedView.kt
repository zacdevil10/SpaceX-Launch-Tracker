package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

interface TwitterFeedView {

    fun updateRecycler(tweets: List<TimelineTweetModel>)

    fun addPagedData(tweets: List<TimelineTweetModel>)

    fun openWebLink(link: String)

    fun showScrollUp()

    fun hideScrollUp()

    fun showProgress()

    fun showPagingProgress()

    fun hideProgress()

    fun hidePagingProgress()

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)
}