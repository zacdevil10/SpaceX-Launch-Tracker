package uk.co.zac_h.spacex.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FairingRecoveryModel(
    val year: Int,
    var successes: Float = 0f,
    var failures: Float = 0f
) : Parcelable