package uk.co.zac_h.spacex.dashboard

import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class DashboardInteractorImpl : DashboardInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getSingleLaunch(id: String, listener: DashboardInteractor.InteractorCallback) {
        scope.launch {

            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getSingleLaunch(id)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onSuccess(id, response.await().body())
                    } else {
                        listener.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {
                    listener.onError(e.localizedMessage)
                } catch (e: Throwable) {
                    listener.onError(e.localizedMessage)
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}