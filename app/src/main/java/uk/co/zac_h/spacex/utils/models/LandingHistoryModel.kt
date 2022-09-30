package uk.co.zac_h.spacex.utils.models

data class LandingHistoryModel(
    val year: Int,
    var ocean: Float = 0f,
    var asds: Float = 0f,
    var rtls: Float = 0f,
    var failures: Float = 0f
)
