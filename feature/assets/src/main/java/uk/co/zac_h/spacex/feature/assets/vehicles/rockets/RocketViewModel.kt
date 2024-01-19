package uk.co.zac_h.spacex.feature.assets.vehicles.rockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import uk.co.zac_h.spacex.core.common.filter.FilterOrder
import uk.co.zac_h.spacex.core.common.filter.RocketFamilyFilter
import uk.co.zac_h.spacex.core.common.filter.RocketTypeFilter
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.types.RocketFamily
import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.core.common.utils.filterAll
import uk.co.zac_h.spacex.core.common.utils.sortedBy
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.get
import javax.inject.Inject

@HiltViewModel
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(RocketsFilterState())
    val filter: StateFlow<RocketsFilterState> = _filter

    private val _rockets: MutableStateFlow<ApiResult<List<RocketItem>>> =
        MutableStateFlow(ApiResult.Pending)
    val rockets: Flow<ApiResult<List<RocketItem>>> = combine(_rockets, _filter) { result, filter ->
        result.map { rocketList ->
            rocketList.filterAll(
                if (filter.family.isFiltered) { rocket ->
                    rocket.family == filter.family.family
                } else null,
                if (filter.type.isFiltered) { rocket ->
                    rocket.type in filter.type.rockets
                } else null
            ).sortedBy(filter.order.order) { rocket -> rocket.maidenFlightMillis }
        }
    }

    init {
        getRockets()
    }

    fun getRockets() {
        _rockets.get(viewModelScope) {
            repository.fetch(key = "agency", cachePolicy = CachePolicy.EXPIRES).map {
                it.launcherList?.map(::RocketItem) ?: emptyList()
            }
        }
    }

    fun updateFilter(
        family: RocketFamily? = null,
        type: List<RocketType>? = null,
        order: Order? = null
    ) {
        _filter.value = _filter.value.copy(
            family = family?.let { RocketFamilyFilter(it) } ?: _filter.value.family,
            type = type?.let { RocketTypeFilter(it) } ?: _filter.value.type,
            order = order?.let { FilterOrder(it) } ?: _filter.value.order
        )
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
