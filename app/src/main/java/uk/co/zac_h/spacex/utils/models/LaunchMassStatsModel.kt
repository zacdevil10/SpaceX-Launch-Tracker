package uk.co.zac_h.spacex.utils.models

data class LaunchMassStatsModel(
    val year: Int,
    var falconOne: OrbitMassModel = OrbitMassModel(),
    var falconNine: OrbitMassModel = OrbitMassModel(),
    var falconHeavy: OrbitMassModel = OrbitMassModel(),
    var starship: OrbitMassModel = OrbitMassModel()
)
