package uk.co.zac_h.spacex.utils.models

import uk.co.zac_h.spacex.utils.RocketTypeOld

data class HistoryStatsModel(
    val rocket: RocketTypeOld,
    var successes: Int = 0,
    var failures: Int = 0,
    var successRate: Int = 0
)