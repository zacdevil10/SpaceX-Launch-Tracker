package uk.co.zac_h.spacex.launches.filter

import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

data class LaunchesFilter(
    val search: StringFilter,
    val dateRange: DateRangeFilter,
    /*val rocket: RocketType? = null,
    val upcoming: Boolean? = null,
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

    init {
        val merge = {
            val stringValue = _search.value
            val dateRangeValue = _date.value

            combine(stringValue, dateRangeValue)
        }

        addSource(_search) { value = merge() }
        addSource(_date) { value = merge() }
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

    private fun combine(vararg filters: Filterable?): LaunchesFilter {
        val stringFilter: StringFilter =
            filters.filterIsInstance<StringFilter>().firstOrNull() ?: StringFilter()
        val dateRangeFilter: DateRangeFilter =
            filters.filterIsInstance<DateRangeFilter>().firstOrNull() ?: DateRangeFilter()

        return LaunchesFilter(stringFilter, dateRangeFilter)
    }

}

@JvmInline
value class StringFilter(val filter: String? = null) : Filterable {

    fun filter(): String? = if (filter.isNullOrEmpty()) null else filter

    fun isFiltered(): Boolean = !filter.isNullOrEmpty()

}

@JvmInline
value class DateRangeFilter(val filter: LongRange? = null) : Filterable {

    fun isFiltered(): Boolean = filter?.isEmpty()?.not() ?: false

}

interface Filterable