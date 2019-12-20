package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.model.reddit.SubredditModel

interface RedditFeedView {

    fun updateRecycler(subredditData: SubredditModel)

}