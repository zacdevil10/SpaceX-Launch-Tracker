package uk.co.zac_h.spacex.filter

import uk.co.zac_h.spacex.types.RocketType

@JvmInline
value class RocketTypeFilter(val rockets: List<RocketType>? = null) : Filter {

    override val isFiltered: Boolean
        get() = !rockets.isNullOrEmpty()
}
