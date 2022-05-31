package uk.co.zac_h.spacex.query

import uk.co.zac_h.spacex.SPACEX_FIELD_CAPSULE_LAUNCHES
import uk.co.zac_h.spacex.SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER
import uk.co.zac_h.spacex.SPACEX_FIELD_LAUNCH_NAME
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel

object VehicleQuery {

    val capsuleQuery = QueryModel(
        "",
        QueryOptionsModel(
            false, listOf(
                QueryPopulateModel(
                    SPACEX_FIELD_CAPSULE_LAUNCHES,
                    select = listOf(
                        SPACEX_FIELD_LAUNCH_NAME,
                        SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER
                    ),
                    populate = ""
                )
            ), "", "", 100000
        )
    )

    val coreQuery = QueryModel(
        options = QueryOptionsModel(
            pagination = false,
            populate = listOf(
                QueryPopulateModel(
                    "launches",
                    select = listOf("name", "flight_number"),
                    populate = ""
                )
            ),
            limit = 100000
        )
    )

    val shipQuery = QueryModel(
        query = "",
        options = QueryOptionsModel(
            false,
            populate = listOf(
                QueryPopulateModel(
                    path = "launches",
                    select = listOf("flight_number", "name"),
                    populate = ""
                )
            ),
            sort = "",
            select = "",
            limit = 200
        )
    )

}