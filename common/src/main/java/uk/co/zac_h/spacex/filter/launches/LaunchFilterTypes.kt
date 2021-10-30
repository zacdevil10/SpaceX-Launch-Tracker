package uk.co.zac_h.spacex.filter.launches

import uk.co.zac_h.spacex.filter.Filterable
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.types.RocketType

@JvmInline
value class StringFilter(val filter: String = "") : Filterable {

    override val isFiltered: Boolean
        get() = filter.isNotEmpty()

}

@JvmInline
value class DateRangeFilter(val filter: LongRange? = null) : Filterable {

    override val isFiltered: Boolean
        get() = filter?.isEmpty()?.not() ?: false

}

@JvmInline
value class LaunchesFilterOrder(val order: Order = Order.ASCENDING) : Filterable {

    override val isFiltered: Boolean
        get() = order != Order.ASCENDING

}

@JvmInline
value class LaunchesFilterRocket(val rockets: List<RocketType>? = null) : Filterable {

    override val isFiltered: Boolean
        get() = !rockets.isNullOrEmpty()

}