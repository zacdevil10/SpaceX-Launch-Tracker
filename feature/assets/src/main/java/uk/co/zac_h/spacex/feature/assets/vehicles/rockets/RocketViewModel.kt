package uk.co.zac_h.spacex.feature.assets.vehicles.rockets

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
import uk.co.zac_h.spacex.core.common.filter.RocketFamilyFilter
import uk.co.zac_h.spacex.core.common.filter.RocketTypeFilter
import uk.co.zac_h.spacex.core.common.filter.toFilter
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.types.RocketFamily
import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.core.common.utils.filterAll
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.withRefresh
import javax.inject.Inject

@HiltViewModel
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(RocketsFilterState())
    val filter: StateFlow<RocketsFilterState> = _filter

    private val refresh = MutableStateFlow<ApiResult<Unit>>(ApiResult.Pending)

    private val _rockets: Flow<ApiResult<List<RocketItem>>> =
        repository.agency
            .map { agency -> agency?.let { it.launcherList?.map(::RocketItem).orEmpty() } }
            .withRefresh(refresh)
    val rockets: Flow<ApiResult<List<RocketItem>>> = combine(_rockets, _filter) { result, filter ->
        result.map { rocketList ->
            rocketList.filterAll(
                if (filter.family.isFiltered) { rocket ->
                    rocket.family == filter.family.value
                } else null,
                if (filter.type.isFiltered) { rocket ->
                    rocket.type in filter.type.value
                } else null
            ).sortedBy(filter.order.value) { rocket -> rocket.maidenFlightMillis }
        }
    }

    init {
        getRockets()
    }

    /** Fetches the agency if the stored one is out of date (it holds the rockets). */
    fun getRockets() {
        refresh.value = ApiResult.Pending
        viewModelScope.launch { refresh.value = repository.refreshAgency() }
    }

    fun filterByRocketFamily(family: RocketFamily) {
        _filter.update {
            it.copy(
                family = if (it.family.value == family) {
                    RocketFamily.NONE.toFilter()
                } else {
                    family.toFilter()
                },
                type = if (family != RocketFamily.FALCON) RocketTypeFilter() else it.type
            )
        }
    }

    fun filterByRocketType(type: RocketType) {
        _filter.update {
            val list = it.type.value.toMutableList()
            if (type in list) list.remove(type) else list.add(type)

            it.copy(type = list.toFilter())
        }
    }

    fun updateOrder(order: Order) {
        _filter.update {
            it.copy(order = order.toFilter())
        }
    }

    fun clearFilter() {
        _filter.value = RocketsFilterState()
    }
}

data class RocketsFilterState(
    val family: RocketFamilyFilter = RocketFamilyFilter(),
    val type: RocketTypeFilter = RocketTypeFilter(),
    val order: FilterOrder = FilterOrder()
)
