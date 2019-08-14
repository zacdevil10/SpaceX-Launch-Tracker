package uk.co.zac_h.spacex.launches

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class LaunchesInteractorImpl : LaunchesInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getLaunches(id: String, order: String, listener: LaunchesInteractor.InteractorCallback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getLaunches(id, order)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onSuccess(response.await().body())
                    } else {
                        listener.onError(response.await().message())
                    }
                } catch (e: HttpException) {
                    listener.onError(e.localizedMessage)
                } catch (e: Throwable) {
                    Log.e(this@LaunchesInteractorImpl.javaClass.name, e.localizedMessage)
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}