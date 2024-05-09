package uk.co.zac_h.spacex.core.common.filter

import uk.co.zac_h.spacex.core.common.types.RocketType

@JvmInline
value class RocketTypeFilter(val value: List<RocketType> = emptyList()) : Filter {

    override val isFiltered: Boolean
        get() = value.isNotEmpty()
}

fun List<RocketType>.toFilter() = RocketTypeFilter(value = this)
