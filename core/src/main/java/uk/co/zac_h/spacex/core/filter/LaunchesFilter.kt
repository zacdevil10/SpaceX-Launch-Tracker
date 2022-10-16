package uk.co.zac_h.spacex.core.filter

data class LaunchesFilter(
    val search: uk.co.zac_h.spacex.core.filter.SearchFilter,
    val dateRange: uk.co.zac_h.spacex.core.filter.DateRangeFilter,
    val order: uk.co.zac_h.spacex.core.filter.FilterOrder,
    val rocket: uk.co.zac_h.spacex.core.filter.RocketTypeFilter
)
