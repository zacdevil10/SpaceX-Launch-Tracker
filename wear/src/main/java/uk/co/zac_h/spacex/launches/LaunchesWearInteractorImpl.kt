package uk.co.zac_h.spacex.launches

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class LaunchesWearInteractorImpl : LaunchesWearInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getAllLaunches(
        id: String,
        order: String,
        listener: LaunchesWearInteractor.Callback
    ) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getLaunches(id, order)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onSuccess(response.await().body())
                    } else {
                        listener.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {
                    listener.onError("Error: ${e.localizedMessage}")
                } catch (e: Throwable) {
                    Log.e(
                        this@LaunchesWearInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelRequest() = coroutineContext.cancel()
}