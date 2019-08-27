package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.util.Log
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
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getLaunchpads()
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onGetLaunchpads(response.await().body())
                    } else {
                        listener.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {
                    listener.onError(
                        e.localizedMessage ?: "There was a network error! Please try refreshing."
                    )
                } catch (e: Throwable) {
                    Log.e(
                        this@PadStatsInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun getLandingPads(listener: PadStatsInteractor.InteractorCallback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getLandingPads()
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        listener.onGetLandingPads(response.await().body())
                    } else {
                        listener.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {
                    listener.onError(
                        e.localizedMessage ?: "There was a network error! Please try refreshing."
                    )
                } catch (e: Throwable) {
                    Log.e(
                        this@PadStatsInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}