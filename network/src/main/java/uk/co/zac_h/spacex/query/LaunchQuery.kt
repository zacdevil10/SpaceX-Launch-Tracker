package uk.co.zac_h.spacex.query

import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel

object LaunchQuery {

    val query = QueryModel(
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

}