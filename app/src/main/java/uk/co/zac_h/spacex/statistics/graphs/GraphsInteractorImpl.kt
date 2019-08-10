package uk.co.zac_h.spacex.statistics.graphs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import uk.co.zac_h.spacex.utils.data.rest.SpaceXInterface

class GraphsInteractorImpl : GraphsInteractor {

    override fun getLaunches(id: String, listener: GraphsInteractor.InteractorCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = SpaceXInterface.create().getLaunches(id)

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

}