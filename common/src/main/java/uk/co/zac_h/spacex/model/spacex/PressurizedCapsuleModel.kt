package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class PressurizedCapsuleModel(
    @field:Json(name = "payload_volume") val payloadVolume: VolumeModel
) : Parcelable