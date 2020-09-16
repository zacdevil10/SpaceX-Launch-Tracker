package uk.co.zac_h.spacex.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrbitMassModel(
    var LEO: Float = 0f,
    var GTO: Float = 0f,
    var PO: Float = 0f,
    var SSO: Float = 0f,
    var ISS: Float = 0f,
    var HCO: Float = 0f,
    var MEO: Float = 0f,
    var SO: Float = 0f,
    var ED_L1: Float = 0f,
    var total: Float = 0f
) : Parcelable