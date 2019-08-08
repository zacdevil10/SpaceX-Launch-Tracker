package uk.co.zac_h.spacex.dashboard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface

class DashboardInteractorImpl : DashboardInteractor {

    override fun getSingleLaunch(id: String, listener: DashboardInteractor.InteractorCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = SpaceXInterface.create().getSingleLaunch(id)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        listener.onSuccess(id, response.body())
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

}