package uk.co.zac_h.spacex.dashboard

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class DashboardInteractorImpl : DashboardInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val active = ArrayList<String>()

    override fun getSingleLaunch(id: String, listener: DashboardInteractor.InteractorCallback) {
        active.add(id)
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getSingleLaunch(id)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        active.remove(id)
                        listener.onSuccess(id, response.await().body())
                    } else {
                        listener.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {
                    listener.onError(
                        e.localizedMessage ?: "There was a network error! Please try refreshing."
                    )
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