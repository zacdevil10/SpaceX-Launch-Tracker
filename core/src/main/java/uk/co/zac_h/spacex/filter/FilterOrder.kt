package uk.co.zac_h.spacex.filter

import uk.co.zac_h.spacex.types.Order

@JvmInline
value class FilterOrder(val order: Order = Order.ASCENDING) : Filter {

    override val isFiltered: Boolean
        get() = order != Order.ASCENDING
}
