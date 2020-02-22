package uk.co.zac_h.spacex.dashboard

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class DashboardInteractorImpl(private val uiContext: CoroutineContext = Dispatchers.Main) :
    DashboardInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + uiContext

    private val scope = CoroutineScope(coroutineContext)

    private val active = ArrayList<String>()

    override fun getSingleLaunch(
        id: String,
        api: SpaceXInterface,
        listener: DashboardInteractor.InteractorCallback
    ) {
        active.add(id)
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                api.getSingleLaunch(id)
            }

            withContext(uiContext) {
                active.remove(id)
                try {
                    if (response.await().isSuccessful) {
                        listener.onSuccess(id, response.await().body())
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
                        this@DashboardInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun hasActiveRequest(): Boolean = active.isNotEmpty()

    override fun cancelAllRequests() = coroutineContext.cancel()
}