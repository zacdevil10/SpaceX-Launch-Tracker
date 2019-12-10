package uk.co.zac_h.spacex.model.spacex

data class StatsPadModel(
    val name: String,
    val attempts: Int,
    val successes: Int,
    val status: String,
    val type: String = "RTLS",
    val isHeading: Boolean = false
)