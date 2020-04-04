package uk.co.zac_h.spacex.news.twitter

import retrofit2.Call
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.rest.TwitterInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class TwitterFeedInteractorImpl : BaseNetwork(), TwitterFeedInteractor {

    private lateinit var call: Call<List<TimelineTweetModel>>

    override fun getTwitterTimeline(
        id: Long?,
        listener: TwitterFeedInteractor.Callback,
        api: TwitterInterface
    ) {
        call = api.getTweets(
            screenName = "SpaceX",
            rts = false,
            trim = false,
            mode = "extended",
            count = 15,
            maxId = id
        ).apply {
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