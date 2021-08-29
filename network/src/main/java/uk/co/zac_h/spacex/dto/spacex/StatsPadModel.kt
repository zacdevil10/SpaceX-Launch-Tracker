package uk.co.zac_h.spacex.dto.spacex

data class StatsPadModel(
    val name: String?,
    val attempts: Int,
    val successes: Int,
    val status: PadStatus?,
    val type: String? = "RTLS",
    val isHeading: Boolean = false
)