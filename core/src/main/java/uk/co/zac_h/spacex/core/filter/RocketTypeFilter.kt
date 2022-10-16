package uk.co.zac_h.spacex.core.filter

import uk.co.zac_h.spacex.core.types.RocketType

@JvmInline
value class RocketTypeFilter(val rockets: List<RocketType>? = null) : Filter {

    override val isFiltered: Boolean
        get() = !rockets.isNullOrEmpty()
}
