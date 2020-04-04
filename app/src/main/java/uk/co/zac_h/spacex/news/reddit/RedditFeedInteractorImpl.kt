package uk.co.zac_h.spacex.news.reddit

import retrofit2.Call
import uk.co.zac_h.spacex.model.reddit.SubredditModel
import uk.co.zac_h.spacex.rest.RedditInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class RedditFeedInteractorImpl : BaseNetwork(), RedditFeedInteractor {

    private lateinit var call: Call<SubredditModel>

    override fun getSubreddit(
        api: RedditInterface,
        listener: RedditFeedInteractor.Callback,
        order: String,
        id: String?
    ) {
        call = api.getRedditFeed(subreddit = "SpaceX", id = id, order = order).apply {
            makeCall {
                onResponseSuccess = {
                    id?.let { _ ->
                        listener.onPagedSuccess(it.body())
                    } ?: listener.onSuccess(it.body())
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = call.cancel()
}