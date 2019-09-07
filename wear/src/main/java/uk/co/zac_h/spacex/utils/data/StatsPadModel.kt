package uk.co.zac_h.spacex.utils.data

data class StatsPadModel(
    val name: String,
    val attempts: Int,
    val successes: Int,
    val status: String,
    val type: String = "RTLS",
    val isHeading: Boolean = false
)