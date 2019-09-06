package uk.co.zac_h.spacex.dashboard

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface
import kotlin.coroutines.CoroutineContext

class DashboardWearInteractorImpl : DashboardWearInteractor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getSingleLaunch(id: String, listener: DashboardWearInteractor.Callback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                SpaceXInterface.create().getSingleLaunch(id)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        when (id) {
                            "next" -> listener.onNextSuccess(response.await().body())
                            "latest" -> listener.onLatestSuccess(response.await().body())
                            else -> Log.e(
                                this@DashboardWearInteractorImpl.javaClass.name,
                                "Invalid launch ID"
                            )
                        }
                    } else {
                        listener.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {

                } catch (e: Throwable) {
                    Log.e(
                        this@DashboardWearInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}