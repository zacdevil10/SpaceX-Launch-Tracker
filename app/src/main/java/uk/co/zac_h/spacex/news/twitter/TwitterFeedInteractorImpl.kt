package uk.co.zac_h.spacex.news.twitter

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.model.twitter.OAuthKeys
import uk.co.zac_h.spacex.rest.TwitterInterface
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class TwitterFeedInteractorImpl :
    TwitterFeedInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getTwitterTimeline(
        listener: TwitterFeedInteractor.Callback,
        oAuthKeys: OAuthKeys
    ) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                TwitterInterface.create(oAuthKeys).getTweets(
                    screenName = "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15
                )
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onSuccess(response.await().body())
                    } else {
                        listener.onError(response.await().message())
                    }
                } catch (e: HttpException) {
                    listener.onError(
                        e.localizedMessage ?: "There was a network error! Please try refreshing."
                    )
                } catch (e: UnknownHostException) {
                    listener.onError("Unable to resolve host! Check your network connection and try again.")
                } catch (e: Throwable) {
                    Log.e(
                        this@TwitterFeedInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun getTwitterTimelineFromId(
        id: Long,
        listener: TwitterFeedInteractor.Callback,
        oAuthKeys: OAuthKeys
    ) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                TwitterInterface.create(oAuthKeys).getTweetsFromId(
                    screenName = "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15,
                    maxId = id
                )
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onPagedSuccess(response.await().body())
                    } else {
                        listener.onError(response.await().message())
                    }
                } catch (e: HttpException) {
                    listener.onError(
                        e.localizedMessage ?: "There was a network error! Please try refreshing."
                    )
                } catch (e: UnknownHostException) {
                    listener.onError("Unable to resolve host! Check your network connection and try again.")
                } catch (e: Throwable) {
                    Log.e(
                        this@TwitterFeedInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}