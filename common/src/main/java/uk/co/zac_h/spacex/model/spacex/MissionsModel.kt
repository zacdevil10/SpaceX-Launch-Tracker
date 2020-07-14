package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MissionsModel(
    @field:Json(name = "flight_number") val flightNumber: Int?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "id") val id: String
) : Parcelable