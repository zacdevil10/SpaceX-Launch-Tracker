package uk.co.zac_h.spacex.launches.filter

import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import uk.co.zac_h.spacex.dto.spacex.RocketType
import javax.inject.Inject

data class LaunchesFilter(
    val search: StringFilter,
    val dateRange: DateRangeFilter,
    val order: LaunchesFilterOrder,
    val rocket: LaunchesFilterRocket,
    /*val upcoming: Boolean? = null,
    val hasCrew: Boolean? = null,
    val landing: Landing? = null,
    val pinned: Boolean? = null*/
) {

    data class Landing(
        val isLanding: Boolean? = null,
        val landingType: String? = null
    )

}

class LaunchesFilterTest @Inject constructor() : MediatorLiveData<LaunchesFilter>() {

    private val _search: MutableLiveData<StringFilter> = MutableLiveData(StringFilter())
    val search: LiveData<StringFilter> = _search

    private val _date: MutableLiveData<DateRangeFilter> = MutableLiveData(DateRangeFilter())
    val date: LiveData<DateRangeFilter> = _date

    private val _order: MutableLiveData<LaunchesFilterOrder> =
        MutableLiveData(LaunchesFilterOrder.ASCENDING)
    val order: LiveData<LaunchesFilterOrder> = _order

    private val _rocketType: MutableLiveData<LaunchesFilterRocket> = MutableLiveData()
    val rocketType: LiveData<LaunchesFilterRocket> = _rocketType

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

    fun updateSearch(value: CharSequence? = null) {
        _search.value = StringFilter(value?.toString().orEmpty())
    }

    fun setRange(start: Long, end: Long) {
        _date.value = DateRangeFilter(start..end)
    }

    fun setRange(range: Pair<Long, Long>) {
        _date.value = DateRangeFilter(range.first..range.second)
    }

    fun clearDateRange() {
        _date.value = DateRangeFilter()
    }

    fun setOrder(order: LaunchesFilterOrder) {
        _order.value = order
    }

    fun setRockets(rockets: List<RocketType>) {
        _rocketType.value = LaunchesFilterRocket(rockets)
    }

    private fun combine(vararg filters: Filterable?): LaunchesFilter {
        val stringFilter: StringFilter =
            filters.filterIsInstance<StringFilter>().firstOrNull() ?: StringFilter()
        val dateRangeFilter: DateRangeFilter =
            filters.filterIsInstance<DateRangeFilter>().firstOrNull() ?: DateRangeFilter()
        val orderFilter: LaunchesFilterOrder =
            filters.filterIsInstance<LaunchesFilterOrder>().firstOrNull()
                ?: LaunchesFilterOrder.ASCENDING
        val rocketsFilter: LaunchesFilterRocket =
            filters.filterIsInstance<LaunchesFilterRocket>().firstOrNull() ?: LaunchesFilterRocket()

        return LaunchesFilter(stringFilter, dateRangeFilter, orderFilter, rocketsFilter)
    }

}

@JvmInline
value class StringFilter(val filter: String? = null) : Filterable {

    fun filter(): String? = if (filter.isNullOrEmpty()) null else filter

    val isFiltered: Boolean
        get() = !filter.isNullOrEmpty()

}

@JvmInline
value class DateRangeFilter(val filter: LongRange? = null) : Filterable {

    val isFiltered: Boolean
        get() = filter?.isEmpty()?.not() ?: false

}

interface Filterable

enum class LaunchesFilterOrder : Filterable {
    ASCENDING, DESCENDING
}

@JvmInline
value class LaunchesFilterRocket(val rockets: List<RocketType>? = null) : Filterable {

    val isFiltered: Boolean
        get() = !rockets.isNullOrEmpty()

}