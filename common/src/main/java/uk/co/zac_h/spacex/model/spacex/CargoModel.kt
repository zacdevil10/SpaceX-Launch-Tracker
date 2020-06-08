package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CargoModel(
    @field:Json(name = "solar_array") val solarArray: Int?,
    @field:Json(name = "unpressurized_cargo") val unpressurizedCargo: Boolean?
) : Parcelable