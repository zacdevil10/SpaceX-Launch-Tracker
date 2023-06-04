package uk.co.zac_h.spacex.core.common.filter

import uk.co.zac_h.spacex.core.common.types.RocketFamily

@JvmInline
value class RocketFamilyFilter(val family: RocketFamily = RocketFamily.NONE) : Filter {

    override val isFiltered: Boolean
        get() = family != RocketFamily.NONE
}