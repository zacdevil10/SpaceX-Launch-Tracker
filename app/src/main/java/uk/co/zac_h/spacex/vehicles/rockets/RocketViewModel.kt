package uk.co.zac_h.spacex.vehicles.rockets

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.sortedBy
import javax.inject.Inject

@HiltViewModel
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    private val _rockets = MutableLiveData<ApiResult<List<Rocket>>>()
    val rockets: LiveData<ApiResult<List<Rocket>>> = _rockets.map { result ->
        result.map {
            it.sortedBy(order) { rocket ->
                rocket.firstFlight
            }
        }
    }

    private var order: Order = Order.ASCENDING

    var selectedId = ""

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getRockets(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_rockets) {
                repository.fetch(key = "rockets", cachePolicy = cachePolicy)
            }

            _rockets.value = response.await().map { result -> result.map { Rocket(it) } }
        }
    }

    fun setOrder(order: Order?) {
        this.order = order ?: Order.ASCENDING
    }
}
