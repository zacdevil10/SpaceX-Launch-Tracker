package uk.co.zac_h.spacex.core.filter

@JvmInline
value class SearchFilter(val filter: String = "") : uk.co.zac_h.spacex.core.filter.Filter {

    override val isFiltered: Boolean
        get() = filter.isNotEmpty()
}
