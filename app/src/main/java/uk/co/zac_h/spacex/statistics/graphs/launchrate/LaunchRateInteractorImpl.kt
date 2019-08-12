package uk.co.zac_h.spacex.statistics.graphs.launchrate

import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class LaunchRateInteractorImpl : LaunchRateInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getLaunches(listener: LaunchRateInteractor.InteractorCallback) {
        scope.launch {
            val response = SpaceXInterface.create().getAllLaunches()

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        listener.onSuccess(response.body())
                    } else {
                        listener.onError(response.message())
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