package uk.co.zac_h.spacex.vehicles.cores

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class CoreInteractorImpl(private val uiContext: CoroutineContext = Dispatchers.Main) :
    CoreInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + uiContext

    private val scope = CoroutineScope(coroutineContext)

    override fun getCores(api: SpaceXInterface, listener: CoreInteractor.Callback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                api.getCores()
            }

            withContext(uiContext) {
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
                        this@CoreInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}