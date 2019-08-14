package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class LaunchHistoryInteractorImpl : LaunchHistoryInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getLaunches(id: String, listener: LaunchHistoryInteractor.InteractorCallback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getLaunches(id, "asc")
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
                    listener.onError(e.localizedMessage)
                }
            }
        }
    }

    override fun getRockets(listener: LaunchHistoryInteractor.InteractorCallback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getRockets()
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onRocketsSuccess(response.await().body())
                    } else {
                        listener.onError(response.await().message())
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