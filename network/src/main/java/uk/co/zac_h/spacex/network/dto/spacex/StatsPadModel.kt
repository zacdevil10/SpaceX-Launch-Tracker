package uk.co.zac_h.spacex.network.dto.spacex

data class StatsPadModel(
    val name: String?,
    val attempts: Int,
    val successes: Int,
    val status: PadStatus?,
    val type: String? = "RTLS"
)
