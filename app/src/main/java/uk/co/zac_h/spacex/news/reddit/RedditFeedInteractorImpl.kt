package uk.co.zac_h.spacex.news.reddit

import retrofit2.Call
import uk.co.zac_h.spacex.model.reddit.RedditPost
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.rest.RedditInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class RedditFeedInteractorImpl : BaseNetwork(), RedditFeedContract.RedditFeedInteractor {

    private var call: Call<SubredditModel>? = null

    override fun getSubreddit(
        api: RedditInterface,
        listener: RedditFeedContract.InteractorCallback,
        order: String,
        id: String?
    ) {
        call = api.getRedditFeed(subreddit = "SpaceX", id = id, order = order).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.data?.children?.map { RedditPost(it.data) }?.also { posts ->
                        id?.let { listener.onPagedSuccess(posts) } ?: listener.onSuccess(posts)
                    }
                }
                onResponseFailure = { if (id == null) listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}