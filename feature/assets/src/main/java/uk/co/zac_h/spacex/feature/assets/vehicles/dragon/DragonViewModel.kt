package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.async
import javax.inject.Inject

@HiltViewModel
class DragonViewModel @Inject constructor(
    private val repository: DragonRepository
) : ViewModel() {

    private val _dragons = MutableLiveData<ApiResult<List<SecondStageItem>>>()
    val dragons: LiveData<ApiResult<List<SecondStageItem>>> = _dragons.map { result ->
        result.map {
            it.sortedBy(order) { spacecraftItem ->
                spacecraftItem.maidenFlightMillis
            }
        }
    }

    private var order: Order = Order.ASCENDING

    var hasOrderChanged = false

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getDragons(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_dragons) {
                repository.fetch(key = "agency", cachePolicy = cachePolicy)
            }

            _dragons.value = response.await().map { result ->
                result.spacecraftList?.map { SecondStageItem(it) } ?: emptyList()
            }
        }
    }

    fun setOrder(order: Order?) {
        hasOrderChanged = true
        this.order = order ?: Order.ASCENDING
    }
}
