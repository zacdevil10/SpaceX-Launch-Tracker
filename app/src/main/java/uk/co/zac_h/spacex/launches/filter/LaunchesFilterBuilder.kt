package uk.co.zac_h.spacex.launches.filter

import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import uk.co.zac_h.spacex.filter.*
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.types.RocketType

class LaunchesFilterBuilder : MediatorLiveData<LaunchesFilter>() {

    private val _search = MutableLiveData(SearchFilter())
    val search: LiveData<SearchFilter> = _search

    private val _date = MutableLiveData(DateRangeFilter())
    val date: LiveData<DateRangeFilter> = _date

    private val _order = MutableLiveData(FilterOrder())
    val order: LiveData<FilterOrder> = _order

    private val _rocketType = MutableLiveData(RocketTypeFilter())
    val rocketType: LiveData<RocketTypeFilter> = _rocketType

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
        _date.value = DateRangeFilter(range.first..range.second)
    }

    fun clearDateFilter() {
        _date.value = DateRangeFilter()
    }

    fun setOrder(order: Order) {
        _order.value = FilterOrder(order)
    }

    fun filterByRocketType(rockets: List<RocketType>) {
        _rocketType.value = RocketTypeFilter(rockets)
    }

    fun clear() {
        _search.value = SearchFilter()
        _date.value = DateRangeFilter()
        _order.value = FilterOrder()
        _rocketType.value = RocketTypeFilter()
    }

    private fun combine(vararg filters: Filter?) = LaunchesFilter(
        filters.filterIsInstance<SearchFilter>().firstOrNull() ?: SearchFilter(),
        filters.filterIsInstance<DateRangeFilter>().firstOrNull() ?: DateRangeFilter(),
        filters.filterIsInstance<FilterOrder>().firstOrNull() ?: FilterOrder(),
        filters.filterIsInstance<RocketTypeFilter>().firstOrNull() ?: RocketTypeFilter()
    )
}
