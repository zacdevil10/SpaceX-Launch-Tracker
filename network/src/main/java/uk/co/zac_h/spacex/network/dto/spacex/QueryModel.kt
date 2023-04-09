package uk.co.zac_h.spacex.network.dto.spacex

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
