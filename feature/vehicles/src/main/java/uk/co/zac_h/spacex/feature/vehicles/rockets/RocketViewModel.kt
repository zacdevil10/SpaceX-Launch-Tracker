package uk.co.zac_h.spacex.feature.vehicles.rockets

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
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    private val _rockets = MutableLiveData<ApiResult<List<LauncherItem>>>()
    val rockets: LiveData<ApiResult<List<LauncherItem>>> = _rockets.map { result ->
        result.map {
            it.sortedBy(order) { rocket -> rocket.maidenFlightMillis }
        }
    }

    private var order: Order = Order.ASCENDING

    var hasOrderChanged = false

    var selectedId = ""

    val cacheLocation: Repository.RequestLocation
        get() = repository.cacheLocation

    fun getRockets(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_rockets) {
                repository.fetch(key = "agency", cachePolicy = cachePolicy)
            }

            _rockets.value = response.await().map { result ->
                result.launcherList?.map { LauncherItem(it) } ?: emptyList()
            }
        }
    }

    fun setOrder(order: Order?) {
        hasOrderChanged = true
        this.order = order ?: Order.ASCENDING
    }
}
