package uk.co.zac_h.spacex.news.twitter

import uk.co.zac_h.spacex.retrofit.TwitterService
import uk.co.zac_h.spacex.BaseNetwork

class TwitterFeedInteractorImpl : BaseNetwork(), TwitterFeedContract.TwitterFeedInteractor {

    //private var call: Call<List<TimelineTweetModel>>? = null

    override fun getTwitterTimeline(
        id: Long?,
        listener: TwitterFeedContract.InteractorCallback,
        api: TwitterService
    ) {
        /*call = api.getTweets(
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
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}