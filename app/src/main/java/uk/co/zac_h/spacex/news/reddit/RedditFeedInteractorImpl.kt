package uk.co.zac_h.spacex.news.reddit

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.RedditInterface
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class RedditFeedInteractorImpl : RedditFeedInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getSubreddit(listener: RedditFeedInteractor.Callback, order: String, id: String?) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                RedditInterface.create().getRedditFeed(subreddit = "SpaceX", id = id, order = order)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        id?.let {
                            listener.onPagedSuccess(response.await().body())
                        } ?: listener.onSuccess(response.await().body())
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
                        this@RedditFeedInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}