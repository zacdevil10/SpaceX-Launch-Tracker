package uk.co.zac_h.spacex.network.query

import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.dto.spacex.QueryOptionsModel

object StatisticsQuery {

    val launchPadQuery = QueryModel(
        "",
        QueryOptionsModel(
            false,
            "",
            "",
            listOf("full_name", "launch_attempts", "launch_successes", "status"),
            100000
        )
    )

    val landingPadQuery = QueryModel(
        "",
        QueryOptionsModel(
            false,
            "",
            "",
            listOf("full_name", "landing_attempts", "landing_successes", "status", "type"),
            100000
        )
    )
}
