package uk.co.zac_h.spacex.utils.models

data class OrbitMassModel(
    var leo: Float = 0f,
    var gto: Float = 0f,
    var sso: Float = 0f,
    var iss: Float = 0f,
    var hco: Float = 0f,
    var meo: Float = 0f,
    var so: Float = 0f,
    var edL1: Float = 0f,
    var other: Float = 0f,
    var total: Float = 0f
)
