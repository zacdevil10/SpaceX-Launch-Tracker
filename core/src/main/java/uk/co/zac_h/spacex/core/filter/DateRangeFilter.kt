package uk.co.zac_h.spacex.core.filter

@JvmInline
value class DateRangeFilter(val filter: LongRange? = null) : uk.co.zac_h.spacex.core.filter.Filter {

    override val isFiltered: Boolean
        get() = filter?.isEmpty()?.not() ?: false
}
