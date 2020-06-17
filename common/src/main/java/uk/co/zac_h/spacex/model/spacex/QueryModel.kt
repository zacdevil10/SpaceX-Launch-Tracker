package uk.co.zac_h.spacex.model.spacex

// v4

data class QueryModel(
    private var options: QueryOptionsModel
)

data class QueryOptionsModel(
    private var pagination: Boolean,
    private var populate: List<QueryPopulateModel>
)

data class QueryPopulateModel(
    private var path: String,
    private var select: Any
)

data class QueryCrewModel(
    private var name: Int,
    private var flight_number: Int
)

data class QueryCoreModel(
    private var name: Int,
    private var flight_number: Int
)

data class QueryCapsulesModel(
    private var name: Int,
    private var flight_number: Int
)

data class QueryLaunchesModel(
    var flightNumber: Int = 0,
    var missionName: Int = 0,
    var launchDateUtc: Int = 0,
    var launchDateUnix: Int = 0,
    var launchDateLocal: Int = 0,
    var datePrecision: Int = 0,
    var staticFireDateUtc: Int = 0,
    var staticFireDateUnix: Int = 0,
    var tbd: Int = 0,
    var net: Int = 0,
    var window: Int = 0,
    var rocket: Int = 0,
    var success: Int = 0,
    var failures: Int = 0,
    var upcoming: Int = 0,
    var details: Int = 0,
    var fairings: Int = 0,
    var crew: Int = 0,
    var ships: Int = 0,
    var capsules: Int = 0,
    var payloads: Int = 0,
    var launchpad: Int = 0,
    var cores: Int = 0,
    var links: Int = 0,
    var autoUpdate: Int = 0
)