package uk.co.zac_h.spacex.network.query

import uk.co.zac_h.spacex.network.dto.spacex.*

object StatisticsQuery {

    val fairingQuery = QueryModel(
        QueryFairingRecovery(true),
        QueryOptionsModel(
            false,
            "",
            QueryLaunchesSortByDate("asc"),
            listOf("fairings", "date_unix"),
            1000000
        )
    )

    val landingHistoryQuery = QueryModel(
        QueryLandingHistory(true),
        QueryOptionsModel(
            false,
            listOf(
                QueryPopulateModel("cores.core", listOf("id"), ""),
                QueryPopulateModel("cores.landpad", listOf("id"), "")
            ),
            QueryLaunchesSortByDate("asc"),
            listOf("cores", "date_local", "date_unix"),
            1000000
        )
    )

    val launchHistoryQuery = QueryModel(
        QueryUpcomingLaunchesModel(false),
        QueryOptionsModel(
            false, listOf(
                QueryPopulateModel(
                    "rocket",
                    populate = "",
                    select = listOf("name", "success_rate_pct")
                )
            ), "", listOf("rocket", "success"), 100000
        )
    )

    val launchMassQuery = QueryModel(
        QueryUpcomingSuccessLaunchesModel(upcoming = false, success = true),
        QueryOptionsModel(
            false,
            listOf(
                QueryPopulateModel(
                    "payloads",
                    populate = "",
                    select = listOf("mass_kg", "orbit")
                ),
                QueryPopulateModel("rocket", populate = "", select = listOf("id", "name"))
            ),
            QueryLaunchesSortByDate("asc"),
            listOf("payloads", "name", "date_unix", "rocket"),
            1000000
        )
    )

    val launchRateQuery = QueryModel(
        "",
        QueryOptionsModel(
            false,
            listOf(
                QueryPopulateModel("rocket", populate = "", select = listOf("id", "name"))
            ),
            QueryLaunchesSortByDate("asc"),
            listOf("rocket", "success", "upcoming", "date_unix"),
            100000
        )
    )

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
