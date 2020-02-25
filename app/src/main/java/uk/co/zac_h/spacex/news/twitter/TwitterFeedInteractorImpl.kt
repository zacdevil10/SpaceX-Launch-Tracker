package uk.co.zac_h.spacex.news.twitter

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.TwitterInterface
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class TwitterFeedInteractorImpl(private val uiContext: CoroutineContext = Dispatchers.Main) :
    TwitterFeedInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + uiContext

    private val scope = CoroutineScope(coroutineContext)

    override fun getTwitterTimeline(
        id: Long?,
        listener: TwitterFeedInteractor.Callback,
        api: TwitterInterface
    ) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                api.getTweets(
                    screenName = "SpaceX",
                    rts = false,
                    trim = false,
                    mode = "extended",
                    count = 15,
                    maxId = id
                )
            }

            withContext(uiContext) {
                try {
                    if (response.await().isSuccessful) {
                        id?.let {
                            listener.onPagedSuccess(response.await().body())
                        } ?: listener.onSuccess(response.await().body())
                    } else {
                        listener.onError("Error: ${response.await().code()}")
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