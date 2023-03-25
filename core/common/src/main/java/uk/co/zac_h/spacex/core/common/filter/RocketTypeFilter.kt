package uk.co.zac_h.spacex.core.common.filter

import uk.co.zac_h.spacex.core.common.types.RocketType

@JvmInline
value class RocketTypeFilter(val rockets: List<RocketType>? = null) : Filter {

    override val isFiltered: Boolean
        get() = !rockets.isNullOrEmpty()
}
