package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

interface TwitterFeedView {

    fun updateRecycler(tweets: List<TimelineTweetModel>)

    fun openWebLink(link: String)
}