package uk.co.zac_h.spacex.model.spacex

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