package uk.co.zac_h.spacex.feature.assets.vehicles.rockets.filter

import uk.co.zac_h.spacex.core.common.filter.FilterOrder
import uk.co.zac_h.spacex.core.common.filter.RocketFamilyFilter
import uk.co.zac_h.spacex.core.common.filter.RocketTypeFilter

data class RocketsFilter(
    val family: RocketFamilyFilter,
    val type: RocketTypeFilter,
    val order: FilterOrder
)
