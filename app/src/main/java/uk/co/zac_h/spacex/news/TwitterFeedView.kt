package uk.co.zac_h.spacex.news

import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel

interface TwitterFeedView {

    fun updateRecycler(tweets: List<TimelineTweetModel>)
}