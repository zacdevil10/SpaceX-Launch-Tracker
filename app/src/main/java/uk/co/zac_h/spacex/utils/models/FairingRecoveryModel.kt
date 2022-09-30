package uk.co.zac_h.spacex.utils.models

data class FairingRecoveryModel(
    val year: Int,
    var successes: Float = 0f,
    var failures: Float = 0f
)
