package uk.co.zac_h.spacex.query

import uk.co.zac_h.spacex.dto.spacex.QueryLaunchesQueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel

object LaunchQuery {

    val launchesQuery = QueryModel(
        options = QueryOptionsModel(
            pagination = false,
            populate = listOf(
                QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                QueryPopulateModel(
                    path = "cores",
                    populate = listOf(
                        QueryPopulateModel(
                            path = "landpad",
                            populate = "",
                            select = listOf("name")
                        ),
                        QueryPopulateModel(
                            path = "core",
                            populate = "",
                            select = listOf("reuse_count")
                        )
                    ),
                    select = ""
                )
            ),
            select = listOf(
                "flight_number",
                "name",
                "date_unix",
                "rocket",
                "cores",
                "links",
                "date_precision",
                "upcoming",
                "tbd"
            ),
            limit = 10000
        )
    )

    fun launchDetailsQuery(launchID: String): QueryModel = QueryModel(
            query = QueryLaunchesQueryModel(_id = launchID),
            options = QueryOptionsModel(
                false,
                populate = listOf(
                    QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                    QueryPopulateModel("rocket", populate = "", select = listOf("name")),
                    QueryPopulateModel(
                        "cores",
                        populate = listOf(
                            QueryPopulateModel(
                                "core",
                                populate = listOf(
                                    QueryPopulateModel(
                                        "launches",
                                        select = listOf("name", "flight_number"),
                                        populate = ""
                                    )
                                ),
                                select = ""
                            ),
                            QueryPopulateModel(
                                "landpad",
                                select = "",
                                populate = listOf(
                                    QueryPopulateModel(
                                        "launches",
                                        select = listOf("id"),
                                        populate = ""
                                    )
                                )
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
                limit = 1
            )
        )

}