package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.model.spacex.LandingPadModel
import uk.co.zac_h.spacex.model.spacex.LaunchpadModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PadType
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class PadStatsInteractorImpl(private val uiContext: CoroutineContext = Dispatchers.Main) :
    PadStatsInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + uiContext

    private val scope = CoroutineScope(coroutineContext)

    @Suppress("UNCHECKED_CAST")
    override fun getPads(
        type: PadType,
        api: SpaceXInterface,
        listener: PadStatsInteractor.InteractorCallback
    ) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                when (type) {
                    PadType.LAUNCH -> api.getLaunchpads()
                    PadType.LANDING -> api.getLandingPads()
                }
            }

            withContext(uiContext) {
                try {
                    if (response.await().isSuccessful) {
                        when (type) {
                            PadType.LAUNCH -> listener.onGetLaunchpads(response.await().body() as List<LaunchpadModel>?)
                            PadType.LANDING -> listener.onGetLandingPads(response.await().body() as List<LandingPadModel>?)
                        }
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
                        this@PadStatsInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}