package uk.co.zac_h.spacex.query

import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel

object CrewQuery {

    val query = QueryModel(
        "",
        QueryOptionsModel(
            false,
            listOf(
                QueryPopulateModel(
                    "launches",
                    select = listOf("flight_number", "name", "date_unix"),
                    populate = ""
                )
            ), "", "", 100000
        )
    )
}
