package uk.co.zac_h.spacex.utils.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchMassStatsModel(
    val year: Int,
    var falconOne: OrbitMassModel = OrbitMassModel(),
    var falconNine: OrbitMassModel = OrbitMassModel(),
    var falconHeavy: OrbitMassModel = OrbitMassModel()
) : Parcelable