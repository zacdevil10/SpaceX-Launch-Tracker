package uk.co.zac_h.spacex.query

import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel

object LaunchQuery {

    val launchesQuery = QueryModel(
        options = QueryOptionsModel(
            false,
            populate = listOf(
                QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                QueryPopulateModel("rocket", populate = "", select = listOf("name")),
                QueryPopulateModel(
                    "cores",
                    populate = listOf(
                        QueryPopulateModel(
                            "landpad",
                            populate = listOf(
                                QueryPopulateModel(
                                    "launches",
                                    populate = "",
                                    select = listOf("id")
                                )
                            ),
                            select = ""
                        ),
                        QueryPopulateModel(
                            "core",
                            populate = listOf(
                                QueryPopulateModel(
                                    "launches",
                                    populate = "",
                                    select = listOf("name", "flight_number")
                                )
                            ),
                            select = ""
                        )
                    ),
                    select = ""
                ),
                QueryPopulateModel(
                    "crew.crew",
                    populate = listOf(
                        QueryPopulateModel(
                            "launches",
                            populate = "",
                            select = listOf("flight_number", "name", "date_unix")
                        )
                    ),
                    select = ""
                ),
                QueryPopulateModel("payloads", populate = "", select = ""),
                QueryPopulateModel(
                    "ships",
                    populate = listOf(
                        QueryPopulateModel(
                            "launches",
                            populate = "",
                            select = listOf("name", "flight_number")
                        )
                    ),
                    select = ""
                )
            ),
            sort = "",
            select = listOf(
                "links",
                "static_fire_date_unix",
                "tbd",
                "net",
                "rocket",
                "details",
                "launchpad",
                "flight_number",
                "name",
                "date_unix",
                "date_precision",
                "upcoming",
                "cores",
                "crew",
                "payloads",
                "ships"
            ),
            limit = 10000
        )
    )
}