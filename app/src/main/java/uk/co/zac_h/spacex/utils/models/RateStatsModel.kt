package uk.co.zac_h.spacex.utils.models

data class RateStatsModel(
    val year: Int,
    var falconOne: Float = 0f,
    var falconNine: Float = 0f,
    var falconHeavy: Float = 0f,
    var failure: Float = 0f,
    var planned: Float = 0f
)
