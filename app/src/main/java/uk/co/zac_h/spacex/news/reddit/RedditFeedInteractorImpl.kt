package uk.co.zac_h.spacex.news.reddit

import uk.co.zac_h.spacex.retrofit.RedditService
import uk.co.zac_h.spacex.BaseNetwork

class RedditFeedInteractorImpl : BaseNetwork(), RedditFeedContract.RedditFeedInteractor {

    //private var call: Call<SubredditModel>? = null

    override fun getSubreddit(
        api: RedditService,
        listener: RedditFeedContract.InteractorCallback,
        order: String,
        id: String?
    ) {
        /*call = api.getRedditFeed(subreddit = "SpaceX", id = id, order = order).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.data?.children?.map { RedditPost(it.data) }?.also { posts ->
                        id?.let { listener.onPagedSuccess(posts) } ?: listener.onSuccess(posts)
                    }
                }
                onResponseFailure = { if (id == null) listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}