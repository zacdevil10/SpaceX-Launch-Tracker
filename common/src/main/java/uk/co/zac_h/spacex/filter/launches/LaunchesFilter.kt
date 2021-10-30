package uk.co.zac_h.spacex.filter.launches

data class LaunchesFilter(
    val search: SearchFilter,
    val dateRange: DateRangeFilter,
    val order: LaunchesFilterOrder,
    val rocket: LaunchesFilterRocket
)
