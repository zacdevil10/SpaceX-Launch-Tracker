package uk.co.zac_h.spacex.feature.assets.vehicles.dragon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.filter.FilterOrder
import uk.co.zac_h.spacex.core.common.filter.toFilter
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.withRefresh
import javax.inject.Inject

@HiltViewModel
class DragonViewModel @Inject constructor(
    private val repository: DragonRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(DragonFilterState())
    val filter: StateFlow<DragonFilterState> = _filter

    private val refresh = MutableStateFlow<ApiResult<Unit>>(ApiResult.Pending)

    private val _dragons: Flow<ApiResult<List<SecondStageItem>>> = repository.agency
        .map { agency -> agency?.let { it.spacecraftList?.map(::SecondStageItem).orEmpty() } }
        .withRefresh(refresh)
    val dragons: Flow<ApiResult<List<SecondStageItem>>> =
        combine(_dragons, _filter) { result, filter ->
            result.map { dragonList ->
                dragonList.sortedBy(filter.order.value) { item -> item.maidenFlightMillis }
            }
        }

    init {
        getDragons()
    }

    /** Fetches the agency if the stored one is out of date (it holds the Dragon spacecraft). */
    fun getDragons() {
        refresh.value = ApiResult.Pending
        viewModelScope.launch { refresh.value = repository.refreshAgency() }
    }

    fun updateOrder(order: Order) {
        _filter.update {
            it.copy(order = order.toFilter())
        }
    }

    fun resetFilter() {
        _filter.value = DragonFilterState()
    }
}

data class DragonFilterState(
    val order: FilterOrder = FilterOrder()
)
