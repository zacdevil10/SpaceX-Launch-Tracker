package uk.co.zac_h.spacex.core.common.filter

import uk.co.zac_h.spacex.core.common.types.Order

@JvmInline
value class FilterOrder(val value: Order = Order.ASCENDING) : Filter {

    override val isFiltered: Boolean
        get() = value != Order.ASCENDING
}

fun Order.toFilter() = FilterOrder(value = this)
