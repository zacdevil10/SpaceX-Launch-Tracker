package uk.co.zac_h.spacex.core.common.filter

@JvmInline
value class DateRangeFilter(val filter: LongRange? = null) : Filter {

    override val isFiltered: Boolean
        get() = filter?.isEmpty()?.not() ?: false
}
