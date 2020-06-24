package uk.co.zac_h.spacex.model.spacex

// v4

data class QueryModel(
    private var query: Any,
    private var options: Any
)

data class QueryOptionsModel(
    private var pagination: Boolean,
    private var populate: List<QueryPopulateModel>,
    private var sort: Any,
    private var select: Any
)

data class QueryPopulateModel(
    private var path: String,
    private var select: Any,
    private var populate: Any
)

data class QueryLaunchesQueryModel(
    private var flight_number: Int
)

data class QueryUpcomingLaunchesModel(
    private var upcoming: Boolean
)

data class QueryLaunchesSortModel(
    private var flight_number: String
)