package uk.co.zac_h.spacex.launches.filter

import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import uk.co.zac_h.spacex.core.filter.SearchFilter
import uk.co.zac_h.spacex.core.types.Order
import uk.co.zac_h.spacex.core.types.RocketType

class LaunchesFilterBuilder : MediatorLiveData<uk.co.zac_h.spacex.core.filter.LaunchesFilter>() {

    private val _search = MutableLiveData(SearchFilter())
    val search: LiveData<SearchFilter> = _search

    private val _date = MutableLiveData(uk.co.zac_h.spacex.core.filter.DateRangeFilter())
    val date: LiveData<uk.co.zac_h.spacex.core.filter.DateRangeFilter> = _date

    private val _order = MutableLiveData(uk.co.zac_h.spacex.core.filter.FilterOrder())
    val order: LiveData<uk.co.zac_h.spacex.core.filter.FilterOrder> = _order

    private val _rocketType = MutableLiveData(uk.co.zac_h.spacex.core.filter.RocketTypeFilter())
    val rocketType: LiveData<uk.co.zac_h.spacex.core.filter.RocketTypeFilter> = _rocketType

    val isFiltered: LiveData<Boolean> = this.map {
        it.search.isFiltered || it.rocket.isFiltered || it.dateRange.isFiltered || it.order.isFiltered
    }

    init {
        val merge = {
            val stringValue = _search.value
            val dateRangeValue = _date.value
            val orderValue = _order.value
            val rocketType = _rocketType.value

            combine(stringValue, dateRangeValue, orderValue, rocketType)
        }

        addSource(_search) { value = merge() }
        addSource(_date) { value = merge() }
        addSource(_order) { value = merge() }
        addSource(_rocketType) { value = merge() }
    }

    fun search(value: CharSequence? = null) {
        _search.value = SearchFilter(value?.toString().orEmpty())
    }

    fun filterByDate(range: Pair<Long, Long>) {
        _date.value = uk.co.zac_h.spacex.core.filter.DateRangeFilter(range.first..range.second)
    }

    fun clearDateFilter() {
        _date.value = uk.co.zac_h.spacex.core.filter.DateRangeFilter()
    }

    fun setOrder(order: Order) {
        _order.value = uk.co.zac_h.spacex.core.filter.FilterOrder(order)
    }

    fun filterByRocketType(rockets: List<RocketType>) {
        _rocketType.value = uk.co.zac_h.spacex.core.filter.RocketTypeFilter(rockets)
    }

    fun clear() {
        _search.value = SearchFilter()
        _date.value = uk.co.zac_h.spacex.core.filter.DateRangeFilter()
        _order.value = uk.co.zac_h.spacex.core.filter.FilterOrder()
        _rocketType.value = uk.co.zac_h.spacex.core.filter.RocketTypeFilter()
    }

    private fun combine(vararg filters: uk.co.zac_h.spacex.core.filter.Filter?) =
        uk.co.zac_h.spacex.core.filter.LaunchesFilter(
            filters.filterIsInstance<SearchFilter>().firstOrNull() ?: SearchFilter(),
            filters.filterIsInstance<uk.co.zac_h.spacex.core.filter.DateRangeFilter>().firstOrNull()
                ?: uk.co.zac_h.spacex.core.filter.DateRangeFilter(),
            filters.filterIsInstance<uk.co.zac_h.spacex.core.filter.FilterOrder>().firstOrNull()
                ?: uk.co.zac_h.spacex.core.filter.FilterOrder(),
            filters.filterIsInstance<uk.co.zac_h.spacex.core.filter.RocketTypeFilter>()
                .firstOrNull() ?: uk.co.zac_h.spacex.core.filter.RocketTypeFilter()
        )
}
