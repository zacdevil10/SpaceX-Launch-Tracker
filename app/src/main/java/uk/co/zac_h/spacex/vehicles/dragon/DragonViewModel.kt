package uk.co.zac_h.spacex.vehicles.dragon

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.async
import uk.co.zac_h.spacex.dto.spacex.Dragon
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.sortedBy
import javax.inject.Inject

@HiltViewModel
class DragonViewModel @Inject constructor(
    private val repository: DragonRepository
) : ViewModel() {

    private val _dragons = MutableLiveData<ApiResult<List<Dragon>>>()
    val dragons: LiveData<ApiResult<List<Dragon>>> = _dragons.map { result ->
        result.map {
            it.sortedBy(order) { dragon ->
                dragon.firstFlight
            }
        }
    }

    private var order: Order = Order.ASCENDING

    var selectedId = ""

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getDragons(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_dragons) {
                repository.fetch(key = "dragons", cachePolicy = cachePolicy)
            }

            _dragons.value = response.await().map { result -> result.map { Dragon(it) } }
        }
    }

    fun setOrder(order: Order?) {
        this.order = order ?: Order.ASCENDING
    }

}