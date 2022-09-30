package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json

// v4

data class QueryModel(
    private var query: Any? = null,
    private var options: Any? = null
)

data class QueryOptionsModel(
    private var pagination: Boolean? = null,
    private var populate: Any? = null,
    private var sort: Any? = null,
    private var select: Any? = null,
    private var limit: Int? = null
)

data class QueryPopulateModel(
    private var path: String,
    private var select: Any,
    private var populate: Any
)

data class QueryLaunchesQueryModel(
    private var _id: String
)

data class QueryUpcomingLaunchesModel(
    private var upcoming: Boolean
)

data class QueryUpcomingSuccessLaunchesModel(
    private var upcoming: Boolean,
    private var success: Boolean
)

data class QueryLaunchesSortModel(
    private var flight_number: String
)

data class QueryLaunchesSortByDate(
    private var date_unix: String
)

data class QueryHistorySort(
    private var event_date_unix: String
)

data class QueryLandingHistory(
    @field:Json(name = "cores.landing_attempt") var landing_attempt: Boolean
)

data class QueryFairingRecovery(
    @field:Json(name = "fairings.recovery_attempt") var recovery_attempt: Boolean
)
