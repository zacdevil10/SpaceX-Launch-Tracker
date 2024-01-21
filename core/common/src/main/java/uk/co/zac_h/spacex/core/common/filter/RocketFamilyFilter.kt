package uk.co.zac_h.spacex.core.common.filter

import uk.co.zac_h.spacex.core.common.types.RocketFamily

@JvmInline
value class RocketFamilyFilter(val value: RocketFamily = RocketFamily.NONE) : Filter {

    override val isFiltered: Boolean
        get() = value != RocketFamily.NONE
}

fun RocketFamily.toFilter() = RocketFamilyFilter(value = this)