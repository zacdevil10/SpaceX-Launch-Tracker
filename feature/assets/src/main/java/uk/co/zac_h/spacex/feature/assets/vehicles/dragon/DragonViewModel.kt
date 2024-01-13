package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import uk.co.zac_h.spacex.core.common.filter.FilterOrder
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.apiFlow
import uk.co.zac_h.spacex.network.toType
import javax.inject.Inject

@HiltViewModel
class DragonViewModel @Inject constructor(
    private val repository: DragonRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(DragonFilterState())
    val filter: StateFlow<DragonFilterState> = _filter

    private val _dragons: Flow<ApiResult<List<SecondStageItem>>> = apiFlow {
        repository.fetch(key = "agency", cachePolicy = CachePolicy.EXPIRES)
    }.toType {
        it.spacecraftList?.map(::SecondStageItem) ?: emptyList()
    }
    val dragons: Flow<ApiResult<List<SecondStageItem>>> =
        combine(_dragons, _filter) { result, filter ->
            result.map { dragonList ->
                dragonList.sortedBy(filter.order.order) { item -> item.maidenFlightMillis }
            }
        }

    fun updateFilter(order: Order) {
        _filter.value = _filter.value.copy(order = FilterOrder(order))
    }

    fun resetFilter() {
        _filter.value = DragonFilterState()
    }
}

data class DragonFilterState(
    val order: FilterOrder = FilterOrder()
)
