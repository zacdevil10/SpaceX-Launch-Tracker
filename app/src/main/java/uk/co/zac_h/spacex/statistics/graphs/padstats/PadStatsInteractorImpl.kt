package uk.co.zac_h.spacex.statistics.graphs.padstats

import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class PadStatsInteractorImpl : PadStatsInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getLaunchpads(listener: PadStatsInteractor.InteractorCallback) {
        scope.launch {
            val response = SpaceXInterface.create().getLaunchpads()

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        listener.onGetLaunchpads(response.body())
                    } else {
                        listener.onError("Error: ${response.code()}")
                    }
                } catch (e: HttpException) {
                    listener.onError(e.localizedMessage)
                } catch (e: Throwable) {
                    listener.onError(e.localizedMessage)
                }
            }
        }
    }

    override fun getLandingPads(listener: PadStatsInteractor.InteractorCallback) {
        scope.launch {
            val response = SpaceXInterface.create().getLandingPads()

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        listener.onGetLandingPads(response.body())
                    } else {
                        listener.onError("Error: ${response.code()}")
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