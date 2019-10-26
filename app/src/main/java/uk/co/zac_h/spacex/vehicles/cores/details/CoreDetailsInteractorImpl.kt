package uk.co.zac_h.spacex.vehicles.cores.details

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class CoreDetailsInteractorImpl : CoreDetailsInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getCoreDetails(
        serial: String,
        listener: CoreDetailsInteractor.InteractorCallback
    ) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getSingleCore(serial)
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
                } catch (e: Throwable) {
                    Log.e(
                        this@CoreDetailsInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}