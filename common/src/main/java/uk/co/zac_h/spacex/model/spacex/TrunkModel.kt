package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrunkModel(
    @field:Json(name = "trunk_volume") val trunkVolume: VolumeModel?,
    @field:Json(name = "cargo") val cargo: CargoModel?
) : Parcelable