package uk.co.zac_h.spacex.models

import uk.co.zac_h.spacex.utils.RocketType

data class HistoryStatsModel(
    val rocket: RocketType,
    var successes: Int = 0,
    var failures: Int = 0,
    var successRate: Int = 0
)