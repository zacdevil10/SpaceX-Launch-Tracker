package uk.co.zac_h.spacex.model.spacex

// v4

data class QueryModel(
    private var query: Any,
    private var options: Any
)

data class QueryOptionsModel(
    private var pagination: Boolean,
    private var populate: Any,
    private var sort: Any,
    private var select: Any,
    private var limit: Int
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