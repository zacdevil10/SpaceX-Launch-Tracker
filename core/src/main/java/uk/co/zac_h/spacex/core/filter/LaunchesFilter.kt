package uk.co.zac_h.spacex.core.filter

data class LaunchesFilter(
    val search: SearchFilter,
    val dateRange: DateRangeFilter,
    val order: FilterOrder,
    val rocket: RocketTypeFilter
)
