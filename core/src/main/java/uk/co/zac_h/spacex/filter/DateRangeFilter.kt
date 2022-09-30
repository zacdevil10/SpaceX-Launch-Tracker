package uk.co.zac_h.spacex.filter

@JvmInline
value class DateRangeFilter(val filter: LongRange? = null) : Filter {

    override val isFiltered: Boolean
        get() = filter?.isEmpty()?.not() ?: false
}
