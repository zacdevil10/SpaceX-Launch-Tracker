package uk.co.zac_h.spacex.launches

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface

class LaunchesInteractorImpl : LaunchesInteractor {

    override fun getLaunches(id: String, listener: LaunchesInteractor.InteractorCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = SpaceXInterface.create().getLaunches(id)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        listener.onSuccess(response.body())
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