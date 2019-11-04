package uk.co.zac_h.spacex.launches.details

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class LaunchDetailsInteractorImpl :
    LaunchDetailsInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getSingleLaunch(id: String, listener: LaunchDetailsInteractor.InteractorCallback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getSingleLaunch(id)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onSuccess(response.await().body())
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
                        this@LaunchDetailsInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelRequest() = coroutineContext.cancel()
}