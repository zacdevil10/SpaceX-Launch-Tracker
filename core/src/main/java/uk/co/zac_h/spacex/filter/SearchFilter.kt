package uk.co.zac_h.spacex.filter

@JvmInline
value class SearchFilter(val filter: String = "") : Filter {

    override val isFiltered: Boolean
        get() = filter.isNotEmpty()
}