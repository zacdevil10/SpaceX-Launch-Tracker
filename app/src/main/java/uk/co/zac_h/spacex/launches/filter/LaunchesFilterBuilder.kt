package uk.co.zac_h.spacex.launches.filter

import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import uk.co.zac_h.spacex.filter.Filterable
import uk.co.zac_h.spacex.filter.launches.*
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.types.RocketType

class LaunchesFilterBuilder : MediatorLiveData<LaunchesFilter>() {

    private val _search = MutableLiveData(StringFilter())
    val search: LiveData<StringFilter> = _search

    private val _date = MutableLiveData(DateRangeFilter())
    val date: LiveData<DateRangeFilter> = _date

    private val _order = MutableLiveData(LaunchesFilterOrder())
    val order: LiveData<LaunchesFilterOrder> = _order

    private val _rocketType = MutableLiveData(LaunchesFilterRocket())
    val rocketType: LiveData<LaunchesFilterRocket> = _rocketType

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
        _search.value = StringFilter(value?.toString().orEmpty())
    }

    fun filterByDate(range: Pair<Long, Long>) {
        _date.value = DateRangeFilter(range.first..range.second)
    }

    fun clearDateFilter() {
        _date.value = DateRangeFilter()
    }

    fun setOrder(order: Order) {
        _order.value = LaunchesFilterOrder(order)
    }

    fun filterByRocketType(rockets: List<RocketType>) {
        _rocketType.value = LaunchesFilterRocket(rockets)
    }

    fun clear() {
        _search.value = StringFilter()
        _date.value = DateRangeFilter()
        _order.value = LaunchesFilterOrder()
        _rocketType.value = LaunchesFilterRocket()
    }

    private fun combine(vararg filters: Filterable?): LaunchesFilter {
        val stringFilter: StringFilter =
            filters.filterIsInstance<StringFilter>().firstOrNull() ?: StringFilter()

        val dateRangeFilter: DateRangeFilter =
            filters.filterIsInstance<DateRangeFilter>().firstOrNull() ?: DateRangeFilter()

        val orderFilter: LaunchesFilterOrder =
            filters.filterIsInstance<LaunchesFilterOrder>().firstOrNull() ?: LaunchesFilterOrder()

        val rocketsFilter: LaunchesFilterRocket =
            filters.filterIsInstance<LaunchesFilterRocket>().firstOrNull() ?: LaunchesFilterRocket()

        return LaunchesFilter(stringFilter, dateRangeFilter, orderFilter, rocketsFilter)
    }

}